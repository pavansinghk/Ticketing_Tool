package view;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

public class Ticketing_Tool {
    public Ticketing_Tool() {
    }

    public String onSignOut() {
        // Add event code here...
        FacesContext fctx = FacesContext.getCurrentInstance();
                  ExternalContext ectx = fctx.getExternalContext();
                          String url = ectx.getRequestContextPath() + "/adfAuthentication?logout=true&end_url=/faces/TicketingTool.jspx";     
                          try {
                            ectx.redirect(url);
                          } catch (IOException e) {
                            e.printStackTrace();
                          }
                          fctx.responseComplete();
        return null;
    }
}
