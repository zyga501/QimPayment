package com.framework.action;

import com.framework.utils.StringUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class AjaxActionSupport extends ActionSupport {
    private final static String AJAXACTIONCOMPLETED = "ajaxActionCompleted";
    private String ajaxActionResult;
    private Map parameterMap;

    public HttpServletResponse getResponse() {
        return (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
    }

    public HttpServletRequest getRequest() {
        return (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
    }

    public String getAttribute(String key) {
        if (getRequest().getSession(false) != null) {
            return StringUtils.convertNullableString(getRequest().getSession(false).getAttribute(key));
        }
        return new String();
    }

    public void setAttribute(String key, Object value) {
        getRequest().getSession(true).setAttribute(key, value);
    }

    public void removeAttribute(String key) {
        if (getRequest().getSession(false) != null) {
            getRequest().getSession(false).removeAttribute(key);
        }
    }

    public void validate() {
        // lazyvalidate
        ActionContext context = ActionContext.getContext();
        parameterMap = context.getParameters();
    }

    public Object getParameter(String paramterName) {
        if (parameterMap == null || parameterMap.size() == 0) {
            return null;
        }

        if (parameterMap.containsKey(paramterName)) {
            return ((Object[]) parameterMap.get(paramterName))[0];
        }

        return null;
    }

    public String getAjaxActionResult() {
        return ajaxActionResult;
    }
    public String AjaxActionComplete() {
        return AJAXACTIONCOMPLETED;
    }
    public String AjaxActionComplete( Map resultMap) {
        ajaxActionResult = JSONObject.fromObject(resultMap).toString();
        return AJAXACTIONCOMPLETED;
    }
}