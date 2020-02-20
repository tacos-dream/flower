package net.somethingnew.kawatan.flower;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;

import net.somethingnew.kawatan.flower.util.ImageCache;
import net.somethingnew.kawatan.flower.util.ImageFetcher;

/**
 * 全体管理クラス
 */
public class GlobalManager extends  FragmentActivity{

    /**
     * 本当は、GlobalManagerでFramgmentActivityのextendsなどしたくないが、
     * ImageFetcherのaddImageCacheの第一引数がFramgmentManagerを要求するので仕方なく…
     */

    public Context mApplicationContext;

    /**
     * サムネイル画像用のディスクキャッシュ
     */
    private static final String IMAGE_CACHE_DIR = "images";
    private ImageFetcher mImageFetcher;

    // GlobalManagerのシングルトンインスタンスの生成は、起動直後のSplashActivityで
    // static関数のonCreateApplication()を呼び出した時点で生成する。
    // 同時にContextを渡してもらい、以後、アプリ内の各所でContext参照できるようにする
    private static GlobalManager sInstance = null;


    /**
     * コンストラクタ.
     */
    private GlobalManager (Context applicationContext)
    {
        this.mApplicationContext = applicationContext;

        /* エラーが出るのでとりあえずコメント
        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(this.mApplicationContext, IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageFetcher = new ImageFetcher(this.mApplicationContext, 64);
        mImageFetcher.addImageCache(getSupportFragmentManager(), cacheParams);
        mImageFetcher.setImageFadeIn(false);

         */

    }

    static void onCreateApplication(Context applicationContext) {
        // Application#onCreateのタイミングでシングルトンが生成される
        sInstance = new GlobalManager(applicationContext);
    }

    /**
     * インスタンス取得.<br>
     * @param
     * @return GlobalManager
     */
    public static GlobalManager getInstance() {
        if (sInstance == null) {
            // SplashActivityからonCreateApplication()が呼び出されシングルトンインスタンスが生成されていれば
            // こんなことは起きないはず…
            throw new RuntimeException("MyContext should be initialized!");
        }
        return sInstance;
    }


    /**
     * 初期化.<br>
     * @param context Context コンテキスト
     * @return
     */
    public void initialize(Context context) {
        try {

        } catch (Exception e) {

        }
    }

    /**
     * @return mImageFetcher ImageFetcher
     */
    public ImageFetcher getImageFetcher() {
        return mImageFetcher;
    }

}
