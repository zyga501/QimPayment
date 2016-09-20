package pf.action;


import framework.action.AjaxActionSupport;
import sun.misc.BASE64Encoder;

import java.io.*;

public class FileAction extends AjaxActionSupport {
    public void DownLoadFile() throws IOException {
        if (null ==  getParameter("filename")) {
            File file = new File(getRequest().getServletContext()
                    .getRealPath("/") + "updatefiles/files.txt");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String readBuffer;
            String updateFiles;
            BASE64Encoder b64 = new BASE64Encoder();
            while ((readBuffer = bufferedReader.readLine()) != null) {
                updateFiles = b64.encode(readBuffer.getBytes());
                getResponse().getWriter().println(updateFiles);
            }
            bufferedReader.close();
            getResponse().getWriter().flush();
            getResponse().getWriter().close();
            return ;
        }
        else {
            String fileName = getParameter("filename").toString().replaceAll("\\\\", "");;
            fileName = fileName.replaceAll("\'", "");
            byte[] buffer = new byte[1024];
            FileInputStream fileInputStream = new FileInputStream(getRequest()
                    .getServletContext().getRealPath("/")
                    + "updatefiles/"
                    + fileName);
            int readSize = fileInputStream.read(buffer);
            while (readSize != -1) {
                getResponse().getOutputStream().write(buffer, 0, readSize);
                readSize = fileInputStream.read(buffer);
            }
            fileInputStream.close();
            getResponse().getOutputStream().flush();
            getResponse().getOutputStream().close();
        }
    }
}
