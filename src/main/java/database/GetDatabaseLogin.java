package database;

import global.Global;
import org.greenrobot.eventbus.Subscribe;
import properties.Properties;

import java.io.*;
import java.util.ArrayList;

public class GetDatabaseLogin {

    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
    private final String PROXY_FILE_NAME = "cloud_sql_proxy";
    private final String CRED_FILE = "clientms-hijama-29f92537f100.json";

    public void getCloudProxy() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (Global.prod) {
            //start the thread to handle cloud sql
            Thread thread = new Thread(() -> {
                Runtime rt = Runtime.getRuntime();
                try {
                    copyFile(cl, PROXY_FILE_NAME + ".exe");
                    copyFile(cl, "clientms-hijama-29f92537f100.json");

                    Process p = rt.exec(TEMP_DIR + PROXY_FILE_NAME + ".exe " +
                            "-instances=clientms-hijama:us-central1:clinic-hijama-main=tcp:3306 " +
                            "-credential_file=" + TEMP_DIR + CRED_FILE);


                } catch (IOException e) {
                    e.getLocalizedMessage();
                }
            });
            thread.setName(PROXY_FILE_NAME);
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.start();
        }
    }

    private void copyFile(ClassLoader cl, String name) throws IOException {
        InputStream is = cl.getResourceAsStream("database/" + name);
        OutputStream os = new FileOutputStream(TEMP_DIR + name);

        byte[] buffer = new byte[1024];
        int length;

        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }

        is.close();
        os.close();
    }


    public void removeFile() {

        try {
            Process p;
//            if os==windows
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                p = Runtime.getRuntime().exec("Taskkill /IM " + PROXY_FILE_NAME + ".exe /F");
                p.waitFor();
                System.out.println("Deleting Files");
            } else {
                //handle linux and macos
                System.out.println("No support for linux bish!");
            }
        } catch (Exception err) {
            err.printStackTrace();
        }

        File exe = new File(TEMP_DIR + PROXY_FILE_NAME + ".exe");
        File key = new File(TEMP_DIR + CRED_FILE);


        key.delete();
        exe.delete();
    }
}
