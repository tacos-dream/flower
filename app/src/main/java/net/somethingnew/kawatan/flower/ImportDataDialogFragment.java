package net.somethingnew.kawatan.flower;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.fasterxml.jackson.databind.ObjectMapper;

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
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImportDataDialogFragment extends DialogFragment {

    GlobalManager globalMgr = GlobalManager.getInstance();
    private View mView;
    TextView mTextViewResultMessage;
    ObjectMapper mObjectMapper = new ObjectMapper();
    private ImportDataDialogFragment.OnDataImportedListener mListener;

    /**
     * Interfaceを定義し、Activity側にoverrideでメソッドを実装させ、Adapter側からそれをCallback的に呼び出せるようにする
     */
    public interface OnDataImportedListener {
        void onDataImported();
    }

    public void setOnDataImportedListener(ImportDataDialogFragment.OnDataImportedListener listener) {
        mListener = listener;
    }

    ImportDataDialogFragment() {
        // 初期値
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.dialog_import_data, container, false);

        mTextViewResultMessage = mView.findViewById(R.id.textViewResultMsg);
        mView.findViewById(R.id.buttonGoBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
                return;
            }
        });

        // TableLayoutのグループを取得
        ViewGroup vg = (ViewGroup) mView.findViewById(R.id.tableLayoutImportData);

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
                executeByConcurrentExecutor(globalMgr.availableBookInfoList.get((int) view.getTag()));
            });
        }

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);

        return mView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtility.d("onDestroy");
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        //以下を呼ぶと親のダイアログが消えてしまうので呼ばないようにする
        //super.onCancel(dialog);
        LogUtility.d("onCancel");
    }

    /**
     * 上記の通り、onCloseの後に呼び出されることはなくなったが、
     * 確認AlertDialogでgetDialog().dismiss()が呼ばれたときはここに来る
     */
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        LogUtility.d("onDismiss");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Dialog dialog = getDialog();

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dialogWidth = (int) (metrics.widthPixels * Constants.DIALOG_FRAGMENT_WIDTH_RATIO);
        int dialogHeight = (int) (metrics.heightPixels * Constants.DIALOG_FRAGMENT_HEIGHT_RATIO);

        lp.width = dialogWidth;
        lp.height = dialogHeight;
        dialog.getWindow().setAttributes(lp);
        LogUtility.d("VersionSettingsDialogFragment: W" + dialogWidth + " x H" + dialogHeight);
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
                resultMessage = "[" + availableBookInfo.getTitle() + "]のインポートが完了しました";
            } else {
                resultMessage = "[" + availableBookInfo.getTitle() + "]のインポートに失敗しました";
            }

            // update UI
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mTextViewResultMessage.setText(resultMessage);
                    mListener.onDataImported();
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
                final InputStreamReader inReader = new InputStreamReader(in, "SJIS");
                final BufferedReader bufferedReader = new BufferedReader(inReader);
                String line = null;

                int iconImageResId = getResources().getIdentifier(Constants.AUTO_ICON_IMAGE_ID[globalMgr.mCategory], "drawable", getActivity().getPackageName());
                int fusenImageResId = getResources().getIdentifier(Constants.DEFAULT_FUSEN_NAME, "drawable", getActivity().getPackageName());
                FolderModel folderModel = new FolderModel(iconImageResId, fusenImageResId);
                folderModel.setTitleName(availableBookInfo.getTitle());
                globalMgr.mCardListMap.put(folderModel.getId(), new LinkedList<>());
                LinkedList<CardModel> cardLinkedList = globalMgr.mCardListMap.get(folderModel.getId());

                while ((line = bufferedReader.readLine()) != null) {
                    StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
                    CardModel cardModel = new CardModel(folderModel.getId());
                    CardDao cardDao = new CardDao(getActivity().getApplicationContext());
                    cardModel.setFrontText(stringTokenizer.nextToken());
                    cardModel.setBackText(stringTokenizer.nextToken());
                    cardLinkedList.add(cardModel);
                    cardDao.insert(cardModel);        // DB上は特に順番は意識しない
//                    LogUtility.d("CardModel: " + mObjectMapper.writeValueAsString(cardModel));
                }
                bufferedReader.close();
                inReader.close();
                in.close();

                // Folderの保存
                folderModel.setNumOfAllCards(cardLinkedList.size());
                globalMgr.mFolderLinkedList.add(0, folderModel);
                FolderDao folderDao = new FolderDao(getActivity().getApplicationContext());
                folderDao.insert(folderModel);
//                LogUtility.d("FolderModel: " + mObjectMapper.writeValueAsString(folderModel));
            }

        } catch (Exception e) {
            LogUtility.d(e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
