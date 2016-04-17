package com.framework.action;


import sun.misc.BASE64Encoder;

import java.io.*;

public class UpgradeAction extends AjaxActionSupport {
    public void DownLoadFile() throws IOException {
        String para ="";
        if (null ==  getParameter("filename")) {
            File fs = new File(getRequest().getServletContext()
                    .getRealPath("/") + "updatefiles/files.txt");
            BufferedReader bufread = new BufferedReader(new FileReader(fs));
            String read = new String("");
            String ff;
            BASE64Encoder b64 = new BASE64Encoder();
            while ((read = bufread.readLine()) != null) {
                ff = b64.encode(read.getBytes());
                getResponse().getWriter().println(ff);
            }
            getResponse().getWriter().flush();
            getResponse().getWriter().close();
            return ;
        } else {
            para =  getParameter("filename").toString().replaceAll("\\\\", "");
            para = para.replaceAll("\'", "");
            byte[] b = new byte[1024];
            FileInputStream fs = new FileInputStream(getRequest()
                    .getServletContext().getRealPath("/")
                    + "updatefiles/"
                    + para);
            int read = fs.read(b);
            while (read != -1) {
                getResponse().getOutputStream().write(b, 0, read);
                read = fs.read(b);
            }
            getResponse().getOutputStream().flush();
            getResponse().getOutputStream().close();
        }
    }
}
