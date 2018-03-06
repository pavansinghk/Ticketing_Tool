package view;

import java.io.FileOutputStream;
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

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.faces.event.ValueChangeEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.RichPopup;

import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.component.rich.input.RichInputListOfValues;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.nav.RichCommandLink;

import oracle.binding.AttributeBinding;
import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.jbo.Row;

import oracle.jbo.ViewObject;
import oracle.jbo.domain.BlobDomain;

import org.apache.myfaces.trinidad.model.UploadedFile;

import weblogic.xml.saaj.util.IOUtils;

public class CreateBean {
    private RichPopup ticketsucess;
    private RichInputText projectname;
    private RichInputText subject;
    private RichSelectOneChoice status;
    private RichSelectOneChoice category;
    private RichSelectOneChoice priority;
    private RichInputText summary;
    private UploadedFile file; 
    private String fileName;
    private String contentType;
    private BlobDomain blob;
    private RichInputText commentName;
    private RichSelectOneChoice projectName;
   
   
    private RichInputText onTicketId;
    private RichInputListOfValues assignTo;


    public CreateBean() {
    }
   
public static void setEL(String el, Object val) 
            {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ELContext elContext = facesContext.getELContext();
                ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
                ValueExpression exp = expressionFactory.createValueExpression(elContext, el, Object.class);
                exp.setValue(elContext, val);
            }


    public void setTicketsucess(RichPopup ticketsucess) {
        this.ticketsucess = ticketsucess;
    }

    public RichPopup getTicketsucess() {
        return ticketsucess;
    }


    public void setProjectname(RichInputText projectname) {
        this.projectname = projectname;
    }

    public RichInputText getProjectname() {
        return projectname;
    }

    public void setSubject(RichInputText subject) {
        this.subject = subject;
    }

    public RichInputText getSubject() {
        return subject;
    }

    public void setStatus(RichSelectOneChoice status) {
        this.status = status;
    }

    public RichSelectOneChoice getStatus() {
        return status;
    }


    public void setCategory(RichSelectOneChoice category) {
        this.category = category;
    }

    public RichSelectOneChoice getCategory() {
        return category;
    }

    public void setPriority(RichSelectOneChoice priority) {
        this.priority = priority;
    }

    public RichSelectOneChoice getPriority() {
        return priority;
    }

    public void setSummary(RichInputText summary) {
        this.summary = summary;
    }

    public RichInputText getSummary() {
        return summary;
    }
private BlobDomain createBlobDomain(UploadedFile file) {
            System.out.println(" create upload method");
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
    public static Object evaluateEL(String el) 
            {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ELContext elContext = facesContext.getELContext();
                ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();   
                ValueExpression exp = expressionFactory.createValueExpression(elContext, el,  Object.class);
                return exp.getValue(elContext);
            }
public void onUpload(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        System.out.println(" on uploadethod");
        file = (UploadedFile)valueChangeEvent.getNewValue();
                 BindingContext ctx = BindingContext.getCurrent();
                 DCBindingContainer bc = (DCBindingContainer)ctx.getCurrentBindingsEntry();
                 DCIteratorBinding attachIter = bc.findIteratorBinding("ActtachmentsTransVO1Iterator");
                 ViewObject attachTransVO = attachIter.getViewObject();
                 Row row = attachTransVO.createRow();
                        row.setAttribute("Attachmentname",file.getFilename());
                        System.out.println(" file name..." +file.getFilename());
                        row.setAttribute("Attachmentcontenttype", file.getContentType());
                        row.setAttribute("Attachmetdata", createBlobDomain(file)); 
                        attachTransVO.insertRow(row);
                     file =null;
                 
             }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setCommentName(RichInputText commentName) {
        this.commentName = commentName;
    }

    public RichInputText getCommentName() {
        return commentName;
    }

    public void onDeleteAttach(ActionEvent actionEvent) {
        // Add event code here...
        BindingContext ctx = BindingContext.getCurrent();
               DCBindingContainer bc = (DCBindingContainer)ctx.getCurrentBindingsEntry();
               DCIteratorBinding attachIter = bc.findIteratorBinding("ActtachmentsTransVO1Iterator");
                 attachIter.getCurrentRow().remove();
    }

    public void setProjectName(RichSelectOneChoice projectName) {
        this.projectName = projectName;
    }

    public RichSelectOneChoice getProjectName() {
        return projectName;
    }
public void setOnTicketId(RichInputText onTicketId) {
        this.onTicketId = onTicketId;
    }

    public RichInputText getOnTicketId() {
        return onTicketId;
    }

    public void onSubmit(ActionEvent actionEvent) {
        // Add event code here...
        System.out.println("before if");  
        String s1 = "Close";
        if( projectName.getValue()!=null && category.getValue()!=null && assignTo.getValue()!=null && status.getValue()!=null && subject.getValue()!=null && priority.getValue()!=null && summary.getValue()!=null) 
       {
        System.out.println("after if");
        DCBindingContainer bindings =(DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding ob = (OperationBinding)bindings.getOperationBinding("CreateTicketingTool");
          ob.getParamsMap().put("fileName", fileName);
       System.out.println("assigned to in bean is "+assignTo.getValue());
          ob.getParamsMap().put("contentType", contentType);
         ob.getParamsMap().put("assignedTo", assignTo.getValue());
          ob.getParamsMap().put("blob", blob);
          ob.execute();
        
          System.out.println(" second method ===>=== ");
        if(ob.getResult() !=null) {
               System.out.println(" Result >> "+ob.getResult());
           if(ob.getResult() != "Error")
            {
                String  ticknum = ob.getResult().toString();
                setEL("#{bindings.Ticketnumber.inputValue}", ticknum);
                RichPopup.PopupHints hints = new RichPopup.PopupHints();
                this.getTicketsucess().show(hints);
            }
        }
       }
           else 
           {
               String messageText= "Required fields are not enterd";
               FacesMessage fm = new FacesMessage(messageText);

               /**
                * set the type of the message.
                * Valid types: error, fatal,info,warning
                */
               fm.setSeverity(FacesMessage.SEVERITY_INFO);
               FacesContext context = FacesContext.getCurrentInstance();
               context.addMessage(null, fm);
               
           }
        
    
        
         
        }

    public void setAssignTo(RichInputListOfValues assignTo) {
        this.assignTo = assignTo;
    }

    public RichInputListOfValues getAssignTo() {
        return assignTo;
    }

   
}
