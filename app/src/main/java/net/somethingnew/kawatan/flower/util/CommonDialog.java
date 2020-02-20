package net.somethingnew.kawatan.flower.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import net.somethingnew.kawatan.flower.R;

/**
 *
 */
public final class CommonDialog {
    /**
     * デフォルトコンストラクタ.<br>
     */
    private CommonDialog(Activity activity) {
    }

    /**
     * 通信プログレスダイアログ表示.<BR>
     * @param activity Activity
     * @param commonDialogCallback CommonDialogCallback
     */
    public static void showConnectionProgressDialog(Activity activity, CommonDialogCallback commonDialogCallback) {
        try {
            // 通信ダイアログを設定
            CustomProgressDialog progressDialogInstance = new CustomProgressDialog(activity);
            progressDialogInstance.show();

            /**
             * CommonDialogThread
             * 汎用ダイアログスレッド
             */
            final class CommonDialogThread extends Thread {
                private Activity activity;
                private CustomProgressDialog progressDialogInstance ;
                private CommonDialogCallback commonDialogCallback;

                /**
                 * コンストラクタ.<br>
                 */
                public CommonDialogThread(Activity activity, CustomProgressDialog progressDialogInstance, CommonDialogCallback commonDialogCallback) {
                    super();

                    this.activity = activity;
                    this.progressDialogInstance = progressDialogInstance;
                    this.commonDialogCallback = commonDialogCallback;
                }

                // 時間のかかる処理をここに記述
                @Override
                public void run() {
                    int returnValue = 0;

                    try {
                        // コールバックインスタンスが存在すればコールする
                        if (commonDialogCallback != null) {
                            returnValue = commonDialogCallback.mainProcess();
                        }
                    } catch (Exception e) {
                        //LogUtility.error(LogUtility.TAG_TACOS_APP, "CommonDialog", e);
                        returnValue = -999;
                    }

                    // ハンドラに結果を通知
                    handler.sendEmptyMessage(returnValue);
                }

                // 処理終了時の動作をここに記述
                private Handler handler = new Handler() {
                    public void handleMessage(Message msg) {
                        try {
                            // プログレスフダイアログを閉じる
                            progressDialogInstance.dismiss();

                            // 0以上をユーザ定義の終了パターンとみなす
                            if (msg.what >= 0) {
                                // コールバックインスタンスが存在すればコールする
                                if (commonDialogCallback != null) {
                                    commonDialogCallback.postProcess(msg);
                                }
                                // -99は通信エラーとみなし、共通の再接続ダイアログを表示
                            } else if (msg.what == -99) {

                                // 自身を再帰呼び出し
                                CommonDialog.showReconnectConfirmDialog(activity, "aaa", "bbb", new CommonDialogCallback() {
                                    @Override
                                    protected void positiveButtonProcess(DialogInterface dialog, int which) {
                                        showConnectionProgressDialog(activity, commonDialogCallback);
                                    }
                                });
                                // -999はアプリケーションエラーとみなし、JSON処理エラーダイアログを表示
                            } else if (msg.what == -999) {
                                throw new Exception();
                            }
                        } catch (Exception e) {
                            // err出力
                            //LogUtility.error(LogUtility.TAG_TACOS_APP, "CommonDialog", e);

                            // 警告ダイアログを表示
                            CommonDialog.showAlertDialog(activity, "aaa", "bbb", new CommonDialogCallback());
                        }
                    }
                };
            }

            // 通信ダイアログ用スレッドの準備
            CommonDialogThread dialogThread = new CommonDialogThread(activity, progressDialogInstance, commonDialogCallback);

            // ダイアログを表示
            dialogThread.start();
        } catch (Exception e) {
            // err出力
            //LogUtility.error(LogUtility.TAG_TACOS_APP, "CommonDialog", e);

            // 警告ダイアログを表示
            CommonDialog.showAlertDialog(activity, "aaa", "bbb", new CommonDialogCallback());
        }
    }

    /**
     * 警告ダイアログ表示.<BR>
     * @param activity Activity
     * @param title String
     * @param message String
     * @param commonDialogCallback CommonDialogCallback
     */
    public static void showAlertDialog(Activity activity, String title, String message, CommonDialogCallback commonDialogCallback) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

            // ダイアログを設定
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(true);

            /**
             * TcsCommonDialogListener
             * ダイアログリスナークラスの準備
             */
            final class TcsCommonDialogListener implements DialogInterface.OnClickListener {
                private CommonDialogCallback commonDialogCallback;

                public TcsCommonDialogListener(CommonDialogCallback commonDialogCallback) {
                    super();

                    this.commonDialogCallback = commonDialogCallback;
                }

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // ダイアログを閉じる
                    dialog.dismiss();

                    // コールバックインスタンスが存在すればコールする
                    if (commonDialogCallback != null) {
                        commonDialogCallback.neutralButtonProcess(dialog, which);
                    }
                }
            }

            // アクションリスナーを設定
            TcsCommonDialogListener listener = new TcsCommonDialogListener(commonDialogCallback);

            // アラートダイアログの否定ボタンのアクションを登録
            alertDialogBuilder.setNegativeButton("aaa", listener);

            // アラートダイアログインスタンスを生成
            AlertDialog alertDialogInstance = alertDialogBuilder.create();

            // アラートダイアログを表示
            alertDialogInstance.show();
        } catch (Exception e) {
            //err出力
            //LogUtility.error(LogUtility.TAG_TACOS_APP, "CommonDialog", e);
        }
    }

    /**
     * 汎用確認ダイアログ表示.<BR>
     * @param activity Activity
     * @param title String
     * @param message String
     * @param positiveButtonName String
     * @param negativeButtonName String
     * @param commonDialogCallback CommonDialogCallback
     */
    public static void showConfirmDialog(Activity activity, String title, String message, String positiveButtonName, String negativeButtonName,
                                         CommonDialogCallback commonDialogCallback) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

            // ダイアログを設定
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(true);

            /**
             * TcsCommonDialogPositiveButtonListener
             * 肯定ボタンのダイアログリスナークラスの準備
             */
            final class TcsCommonDialogPositiveButtonListener implements DialogInterface.OnClickListener {
                private CommonDialogCallback commonDialogCallback;

                /**
                 * コンストラクタ.<BR>
                 * @param commonDialogCallback CommonDialogCallback
                 */
                public TcsCommonDialogPositiveButtonListener(CommonDialogCallback commonDialogCallback) {
                    super();

                    this.commonDialogCallback = commonDialogCallback;
                }

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // ダイアログを閉じる
                    dialog.dismiss();

                    // コールバックインスタンスが存在すればコールする
                    if (commonDialogCallback != null) {
                        commonDialogCallback.positiveButtonProcess(dialog, which);
                    }
                }
            }

            // アクションリスナーを設定
            TcsCommonDialogPositiveButtonListener positiveButtonListener = new TcsCommonDialogPositiveButtonListener(commonDialogCallback);

            // アラートダイアログの肯定ボタンのアクションを登録
            alertDialogBuilder.setPositiveButton(positiveButtonName, positiveButtonListener);

            /**
             * TcsCommonDialogNegativeButtonListener
             * 否定ボタンのダイアログリスナークラスの準備
             */
            final class TcsCommonDialogNegativeButtonListener implements DialogInterface.OnClickListener {
                private CommonDialogCallback commonDialogCallback;

                /**
                 * コンストラクタ.<BR>
                 * @param commonDialogCallback CommonDialogCallback
                 */
                public TcsCommonDialogNegativeButtonListener(CommonDialogCallback commonDialogCallback) {
                    super();

                    this.commonDialogCallback = commonDialogCallback;
                }

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // ダイアログを閉じる
                    dialog.dismiss();

                    // コールバックインスタンスが存在すればコールする
                    if (commonDialogCallback != null) {
                        commonDialogCallback.negativeButtonProcess(dialog, which);
                    }
                }
            }

            // アクションリスナーを設定
            TcsCommonDialogNegativeButtonListener negativeButtonListener = new TcsCommonDialogNegativeButtonListener(commonDialogCallback);

            // アラートダイアログの否定ボタンのアクションを登録
            alertDialogBuilder.setNegativeButton(negativeButtonName, negativeButtonListener);

            // アラートダイアログインスタンスを生成
            AlertDialog alertDialogInstance = alertDialogBuilder.create();

            // アラートダイアログを表示
            alertDialogInstance.show();
        } catch (Exception e) {
            //err出力
            //LogUtility.error(LogUtility.TAG_TACOS_APP, "CommonDialog", e);
        }
    }

    /**
     * 通信再試行確認ダイアログ表示.<BR>
     */
    private static void showReconnectConfirmDialog(Activity activity, String title, String message, CommonDialogCallback commonDialogCallback) {
        try {
            showConfirmDialog(activity, title, message, "aaa", "bbb", commonDialogCallback);
        } catch (Exception e) {
            //err出力
            //LogUtility.error(LogUtility.TAG_TACOS_APP, "CommonDialog", e);
        }
    }

    /**
     * CustomProgressDialog
     * 共通プログレスダイアログ
     */
    public static class CustomProgressDialog extends Dialog {

        public CustomProgressDialog(Context context) {
            super(context, R.style.Theme_CustomProgressDialog);
            this.setContentView(R.layout.custom_progress_dialog);
            this.setCancelable(true);
        }
    }
}