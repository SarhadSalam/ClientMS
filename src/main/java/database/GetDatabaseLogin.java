package database;

import global.Global;

import java.io.*;

public class GetDatabaseLogin {

    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    public void getCloudProxy() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        if (Global.prod) {
            //start the thread to handle cloud sql
            Thread thread = new Thread(() -> {
                Runtime rt = Runtime.getRuntime();
                try {
                    copyFile(cl, "cloud_sql_proxy.exe");
                    copyFile(cl, "clientms-hijama-29f92537f100.json");

                    Process p = rt.exec(TEMP_DIR + "cloud_sql_proxy.exe " +
                            "-instances=clientms-hijama:us-central1:clinic-hijama-main=tcp:3306 " +
                            "-credential_file=" + TEMP_DIR + "clientms-hijama-29f92537f100.json");

                    System.out.println("Done");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.setName("cloud_sql_proxy");
            thread.setPriority(1);
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


    public void removeFile(){
        File exe = new File(TEMP_DIR +"cloud_sql_proxy.exe");
        File key = new File(TEMP_DIR +"clientms-hijama-29f92537f100.json");
        exe.delete();
        key.delete();
    }
}
