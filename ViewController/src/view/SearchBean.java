package view;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.sql.SQLException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.nav.RichCommandLink;

import oracle.binding.OperationBinding;

import oracle.jbo.ViewObject;
import oracle.jbo.domain.BlobDomain;

import org.apache.myfaces.trinidad.model.UploadedFile;

import weblogic.servlet.security.ServletAuthentication;


public class SearchBean {
        private RichCommandLink onClick;
        private RichPopup formUpdate;
        private UploadedFile file; 
        private String fileName;
        private String contentType;
        private BlobDomain blob;
    private RichInputText commentName;
    private RichSelectOneChoice defaultInProgress;
    private RichInputText stepsToReproduce;
    private RichInputText additionalInformation;
    private RichInputFile attachFile;

    public SearchBean() {
    }
    
    public static String toLogout() throws IOException {
         // Add event code here...
         ExternalContext ectx =
         FacesContext.getCurrentInstance().getExternalContext();
         HttpServletResponse response = (HttpServletResponse)ectx.getResponse();
         HttpSession session = (HttpSession)ectx.getSession(false);
         session.invalidate();

         response.sendRedirect("Login.html");
         return null;
     }
    public void setOnClick(RichCommandLink onClick) {
        this.onClick = onClick;
    }
    public RichCommandLink getOnClick() {
        return onClick;
    }
    public void onLink(ActionEvent actionEvent) {
        System.out.println(" on Ticket Number ");
        BindingContext ctx = BindingContext.getCurrent();
        DCBindingContainer bc = (DCBindingContainer)ctx.getCurrentBindingsEntry();
        DCIteratorBinding iterator = bc.findIteratorBinding("TicketingtoolVO1Iterator");
         ViewObject vo = iterator.getViewObject();
        System.out.println("ticket id..  "+evaluateEL("#{pageFlowScope.ticketId}"));
        vo.setWhereClause(" TicketingtoolEO.TICKETID = "+evaluateEL("#{pageFlowScope.ticketId}"));
        if( defaultInProgress.getValue().equals("Open")  || defaultInProgress.getValue().equals("New") || defaultInProgress.getValue().equals("InProgress")){
        defaultInProgress.setValue("InProgress");
        }
        else{
            defaultInProgress.setReadOnly(true);
           
        }
         vo.executeQuery();
        iterator = bc.findIteratorBinding("AttachmentsRVO1Iterator");
               ViewObject vo1 = iterator.getViewObject();
               vo1.setWhereClause(" TICKETID = "+evaluateEL("#{pageFlowScope.ticketId}"));
               vo1.executeQuery();
               System.out.println(" attach count .. "+vo1.getRowCount());
         
        iterator = bc.findIteratorBinding("CommentsVO1Iterator");
        System.out.println("entering Iterator");
              ViewObject vo2 = iterator.getViewObject();
              vo2.setWhereClause(" CommentsEO.TICKETID =" +evaluateEL("#{pageFlowScope.ticketId}"));
              System.out.println("in commentsvo1 iterator "+vo2.getQuery());
               vo2.executeQuery();
               System.out.println(" -=-=-=-=-=-=- ");
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        this.getFormUpdate().show(hints);
    
    }
        public static Object evaluateEL(String el) 
        {
            System.out.println(" el string "+el);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ELContext elContext = facesContext.getELContext();
            ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();   
            ValueExpression exp = expressionFactory.createValueExpression(elContext, el,  Object.class);
            return exp.getValue(elContext);
        }

    public void onPopupSubmit(ActionEvent actionEvent) {
        // Add event code here...
        String s1 = "Close";
        DCBindingContainer bindings =(DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
               OperationBinding ob = (OperationBinding)bindings.getOperationBinding("searchSubmit");
            if(defaultInProgress.getValue().equals(s1)) {
               
                  DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                  Date date = new Date();
                  dateFormat.format(date);
                  System.out.println("date is "+date);
                  ob.getParamsMap().put("closeddate",date);
                      defaultInProgress.setReadOnly(true);
                    
                  }
            ob.execute();    
            
            
        }
    
    public static void setEL(String el, Object val) 
        {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ELContext elContext = facesContext.getELContext();
            ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
            ValueExpression exp = expressionFactory.createValueExpression(elContext, el, Object.class);
            exp.setValue(elContext, val);
        }
public void setFormUpdate(RichPopup formUpdate) {
        this.formUpdate = formUpdate;
    }

    public RichPopup getFormUpdate() {
        return formUpdate;
        }
    private BlobDomain createBlobDomain(UploadedFile file) {
           InputStream in = null;
           BlobDomain blobDomain = null;
           OutputStream out = null;
           try
           {                
               in = file.getInputStream();              
               blobDomain = new BlobDomain();               
               out = blobDomain.getBinaryOutputStream();                
               org.apache.commons.io.IOUtils.copy(in, out);
           }
           catch (IOException e)
           {
               e.printStackTrace();
           }
           catch (SQLException e)
           {
               e.fillInStackTrace();
           }           
           return blobDomain;
       }

    public void onUpload(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        file = (UploadedFile)valueChangeEvent.getNewValue();
                          // Get the file name
                          fileName = file.getFilename();
                          System.out.println(" file name..."+fileName);
                          // get the mime type
                          contentType = file.getContentType();
                          System.out.println(" content type.. "+contentType);
                          // get blob
                          blob=createBlobDomain(file); 
                          System.out.println(" blob type .."+blob);
        DCBindingContainer bindings =(DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding ob = (OperationBinding)bindings.getOperationBinding("updateAttachment");
        ob.getParamsMap().put("ticId", evaluateEL("#{pageFlowScope.ticketId}"));
                         ob.getParamsMap().put("fileName", fileName);
                         ob.getParamsMap().put("contentType", contentType);
                         ob.getParamsMap().put("blob", blob);
         ob.execute();
    }



    public void onComment(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        DCBindingContainer bindings =(DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();    
               OperationBinding ob = (OperationBinding)bindings.getOperationBinding("onPopupComments");
               ob.getParamsMap().put("ticId", evaluateEL("#{pageFlowScope.ticketId}"));
               ob.getParamsMap().put("comments",commentName.getValue());
               ob.execute();
    }

    public void setCommentName(RichInputText commentName) {
        this.commentName = commentName;
    }

    public RichInputText getCommentName() {
        return commentName;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void onDelete(ActionEvent actionEvent) {
        // Add event code here...
        BindingContext ctx = BindingContext.getCurrent();
               DCBindingContainer bc = (DCBindingContainer)ctx.getCurrentBindingsEntry();
               DCIteratorBinding iterator = bc.findIteratorBinding("AttachmentsRVO1Iterator");
               String attid =iterator.getCurrentRow().getAttribute("AttachmentId").toString();
               System.out.println(" delete attachment "+attid);
               DCBindingContainer bindings =(DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
               OperationBinding ob = (OperationBinding)bindings.getOperationBinding("forDelete");
               ob.getParamsMap().put("AttachId", attid);
               ob.getParamsMap().put("ticId", evaluateEL("#{pageFlowScope.ticketId}"));
               ob.execute();
    }

    public void setDefaultInProgress(RichSelectOneChoice defaultInProgress) {
        this.defaultInProgress = defaultInProgress;
    }

    public RichSelectOneChoice getDefaultInProgress() {
        return defaultInProgress;
    }

    public void onSearch(ActionEvent actionEvent) {
        // Add event code here...
        DCBindingContainer bindings =(DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding ob = (OperationBinding)bindings.getOperationBinding("onSearch");
               ob.execute();
    }

    public void setStepsToReproduce(RichInputText stepsToReproduce) {
        this.stepsToReproduce = stepsToReproduce;
    }

    public RichInputText getStepsToReproduce() {
        return stepsToReproduce;
    }

    public void setAdditionalInformation(RichInputText additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public RichInputText getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAttachFile(RichInputFile attachFile) {
        this.attachFile = attachFile;
    }

    public RichInputFile getAttachFile() {
        return attachFile;
    }

    public void onLogout(ActionEvent actionEvent) throws IOException {
        // Add event code here...
        FacesContext fc = FacesContext.getCurrentInstance();
               ExternalContext ectx = fc.getExternalContext();       
               HttpSession session = (HttpSession)ectx.getSession(false);
               try {
                   session.invalidate();
                   ectx.redirect("login.html");
                   fc.responseComplete();
               } catch (Exception exp) {
                   ectx.redirect("login.html");
                   fc.responseComplete();
               }

    }

    public void onReset(ActionEvent actionEvent) {
        // Add event code here...
        System.out.println("inside reset");
        BindingContext ctx = BindingContext.getCurrent();
        DCBindingContainer bc = (DCBindingContainer)ctx.getCurrentBindingsEntry();
        DCIteratorBinding iterator = bc.findIteratorBinding("TicketingToolTransVO1Iterator");
        iterator.getViewObject().executeQuery();
    }
}
