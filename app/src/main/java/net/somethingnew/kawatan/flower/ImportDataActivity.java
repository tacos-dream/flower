package net.somethingnew.kawatan.flower;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.somethingnew.kawatan.flower.db.dao.CardDao;
import net.somethingnew.kawatan.flower.db.dao.FolderDao;
import net.somethingnew.kawatan.flower.model.AvailableBookInfo;
import net.somethingnew.kawatan.flower.model.CardModel;
import net.somethingnew.kawatan.flower.model.FolderModel;
import net.somethingnew.kawatan.flower.util.LogUtility;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImportDataActivity extends AppCompatActivity {

    GlobalManager globalMgr = GlobalManager.getInstance();
    TextView mTextViewResultMessage;
    ObjectMapper mObjectMapper = new ObjectMapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_import_data);
        mTextViewResultMessage = findViewById(R.id.textViewResultMsg);
        findViewById(R.id.buttonGoBack).setOnClickListener(view -> {
            finish();
        });

        // TableLayoutのグループを取得
        ViewGroup vg = (ViewGroup) findViewById(R.id.tableLayoutImportData);

        // 階層構造をgetChildAt()で辿っていく
        for (int i = 0; i < globalMgr.availableBookInfoList.size(); i++) {
            // 行を追加
            getLayoutInflater().inflate(R.layout.tablerow_importdata, vg);
            // コンテンツの表示
            TableRow tr = (TableRow) vg.getChildAt(i);
            LinearLayout linearLayout = (LinearLayout) (tr.getChildAt(0));
            ((TextView) (linearLayout.getChildAt(0))).setText(globalMgr.availableBookInfoList.get(i).getTitle());
            Button btn = ((Button) (linearLayout.getChildAt(1)));
            btn.setTag(i);
            btn.setOnClickListener(view -> {
                executeByConcurrentExecutor(globalMgr.availableBookInfoList.get((int)view.getTag()));
            });
        }
    }

    // AsyncTaskがdeprecatedなので、以下のサイトを参考に実装
    // Handlerもどうにかしないといけないが笑
    // https://tips.priart.net/52/
    private void executeByConcurrentExecutor(AvailableBookInfo availableBookInfo) {
        final Handler handler = new Handler();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // do something in background
            final String resultMessage;
            if (downloadCsvAndImport(availableBookInfo)) {
                resultMessage = "インポートが完了しました。";
            } else {
                resultMessage = "インポートに失敗しました。";
            }

            // update UI
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mTextViewResultMessage.setText(resultMessage);
                }
            });
        });
    }
    // AsyncTaskがdeprecatedなので、とりあえずバックグラウンドは無しでやってみる
    public boolean downloadCsvAndImport(AvailableBookInfo availableBookInfo) {
        final int CONNECTION_TIMEOUT = 30 * 1000;
        final int READ_TIMEOUT = 30 * 1000;

        HttpURLConnection conn = null;
        try {
            URL url = new URL(availableBookInfo.getUrl());
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);

            conn.setRequestMethod("GET");
            conn.connect();
            int statusCode = conn.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                //responseの読み込み
                final InputStream in = conn.getInputStream();
//            final String encoding = conn.getContentEncoding();
                final InputStreamReader inReader = new InputStreamReader(in, "UTF-8");
                final BufferedReader bufferedReader = new BufferedReader(inReader);
                String line = null;

                int iconImageResId = getResources().getIdentifier(Constants.AUTO_ICON_IMAGE_ID[globalMgr.mCategory], "drawable", getPackageName());
                int fusenImageResId = getResources().getIdentifier(Constants.DEFAULT_FUSEN_NAME, "drawable", getPackageName());
                FolderModel folderModel = new FolderModel(iconImageResId, fusenImageResId);
                folderModel.setTitleName(availableBookInfo.getTitle());
                globalMgr.mCardListMap.put(folderModel.getId(), new LinkedList<>());
                LinkedList<CardModel> cardLinkedList = globalMgr.mCardListMap.get(folderModel.getId());

                int count = 0;
                while ((line = bufferedReader.readLine()) != null) {
                    LogUtility.d(line);
                    StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
                    CardModel cardModel = new CardModel(folderModel.getId());
                    CardDao cardDao = new CardDao(getApplicationContext());
                    cardModel.setFrontText(stringTokenizer.nextToken());
                    cardModel.setBackText(stringTokenizer.nextToken());
                    cardLinkedList.add(cardModel);
                    cardDao.insert(cardModel);        // DB上は特に順番は意識しない
                    LogUtility.d("CardModel: " + mObjectMapper.writeValueAsString(cardModel));
                    count++;
                    if (count == 2) { break;}
                }
                bufferedReader.close();
                inReader.close();
                in.close();

                // Folderの保存
                folderModel.setNumOfAllCards(cardLinkedList.size());
                globalMgr.mFolderLinkedList.add(0, folderModel);
                FolderDao folderDao = new FolderDao(getApplicationContext());
                folderDao.insert(folderModel, 0);
                LogUtility.d("FolderModel: " + mObjectMapper.writeValueAsString(folderModel));
            }

        } catch (Exception e) {
            LogUtility.d(e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;
    }

}