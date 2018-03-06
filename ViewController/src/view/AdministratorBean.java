package view;

import java.text.DateFormat;

import java.text.SimpleDateFormat;


import java.util.Calendar;
import java.util.Date;

import javax.el.ELContext;

import javax.el.ExpressionFactory;

import javax.el.ValueExpression;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.faces.event.ValueChangeEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.OperationBinding;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputListOfValues;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.jbo.ViewObject;

public class AdministratorBean {
    private RichInputText projectName;
    private RichPopup projectSuccess;
    private RichPopup addUserSuccess;
    private RichSelectOneChoice onProjectNameSuccess;
    private RichInputListOfValues userName;
    private RichCommandButton addUserbtn;
    private RichInputDate createProjectStartDate;
    private RichInputDate createProjectEndDate;
    private RichOutputText addUserActive;
    private RichOutputText addUserInactive;
    public oracle.jbo.domain.Date getCurrentDate()  
    {  
      
    oracle.jbo.domain.Date cdate=new oracle.jbo.domain.Date(new java.sql.Date(new java.util.Date().getTime()));  
    return cdate;  
      
    }  

    public Date getMinDate() {
           try {
         Calendar cal = Calendar.getInstance();
           java.util.Date date = cal.getTime();
           DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
               System.out.println("start date is "+createProjectStartDate.getValue());
        String currentDate = formatter.format(createProjectStartDate.getValue());
                Date minDate = formatter.parse(currentDate);

                return formatter.parse(currentDate);
                   } catch (Exception e) {
           return null;
                   }
               }
    public AdministratorBean() {
    }

    public void setProjectName(RichInputText projectName) {
        this.projectName = projectName;
    }

    public RichInputText getProjectName() {
        return projectName;
    }

    public void onSubmit(ActionEvent actionEvent) {
        // Add event code here...
        System.out.println("project name .."+projectName.getValue());
                if(projectName.getValue() !=null  &&  projectName.getValue().toString().trim() != "  "&& createProjectStartDate.getValue() != null && createProjectEndDate.getValue() != null){
                System.out.println("  got project name ");
                    DCBindingContainer bindings =(DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
                    OperationBinding ob = (OperationBinding)bindings.getOperationBinding("onAdminProjectName");
                    System.out.println("name is"+projectName.getValue());
                    ob.getParamsMap().put("projectName",projectName.getValue());
                    System.out.println("start date is "+ createProjectStartDate.getValue() );
                    ob.getParamsMap().put("startdate", createProjectStartDate.getValue());
                    ob.getParamsMap().put("enddate", createProjectStartDate.getValue());
                    ob.execute();
                    System.out.println(" after onsubmit commit -->-- ");
                    RichPopup.PopupHints hints = new RichPopup.PopupHints();
                    projectSuccess.show(hints);
                    
                } 
                
                else {
                    String messageText= "Required Feilds are not entered";
                    FacesMessage fm = new FacesMessage(messageText);
                    fm.setSeverity(FacesMessage.SEVERITY_INFO);
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, fm);
                    
                }
    }

    public void setProjectSuccess(RichPopup projectSuccess) {
        this.projectSuccess = projectSuccess;
    }

    public RichPopup getProjectSuccess() {
        return projectSuccess;
    }

    public void onAddUser(ActionEvent actionEvent) {
        // Add event code here...
        if (onProjectNameSuccess.getValue().toString().trim() != "" &&  userName.getValue().toString().trim() != "" ) {
         DCBindingContainer bindings =(DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
         OperationBinding ob = (OperationBinding)bindings.getOperationBinding("addUser");
         ob.execute();
          RichPopup.PopupHints hints = new RichPopup.PopupHints();
          addUserSuccess.show(hints);
        }
        else {
                  
                  String messageText= "Required fields are not entered";
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

    public void setAddUserSuccess(RichPopup addUserSuccess) {
        this.addUserSuccess = addUserSuccess;
    }

    public RichPopup getAddUserSuccess() {
        return addUserSuccess;
    }
           public static Object evaluateEL(String el) 
               {
                   FacesContext facesContext = FacesContext.getCurrentInstance();
                   ELContext elContext = facesContext.getELContext();
                   ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();   
                   ValueExpression exp = expressionFactory.createValueExpression(elContext, el,  Object.class);
                   return exp.getValue(elContext);
               }
    public void setOnProjectNameSuccess(RichSelectOneChoice onProjectNameSuccess) {
        this.onProjectNameSuccess = onProjectNameSuccess;
    }

    public RichSelectOneChoice getOnProjectNameSuccess() {
        return onProjectNameSuccess;
    }
    public void setUserName(RichInputListOfValues userName) {
        this.userName = userName;
    }

    public RichInputListOfValues getUserName() {
        return userName;
    }

    public void setAddUserbtn(RichCommandButton addUserbtn) {
        this.addUserbtn = addUserbtn;
    }

    public RichCommandButton getAddUserbtn() {
        return addUserbtn;
    }

    public void addUser(ActionEvent actionEvent) {
        // Add event code here...
        addUserbtn.setVisible(true);
        userName.setVisible(true);
    }

    public void projectSelected(ValueChangeEvent valueChangeEvent) {
        // Add event code her
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        BindingContext ctx = BindingContext.getCurrent();
        DCBindingContainer bc = (DCBindingContainer)ctx.getCurrentBindingsEntry();
        DCIteratorBinding iterator = bc.findIteratorBinding("ProjectsVO1Iterator");
        ViewObject vo = iterator.getViewObject();
        vo.setWhereClause("ProjectsEO.PROJECT_NAME = '"+onProjectNameSuccess.getValue()+"'");
        vo.executeQuery();
        System.out.println("start date is "+ vo.first().getAttribute("StartDate"));
        oracle.jbo.domain.Date startDate = (oracle.jbo.domain.Date)getCurrentDate();
        System.out.println("end date is "+ vo.first().getAttribute("EndDate") );
        oracle.jbo.domain.Date endDate = (oracle.jbo.domain.Date) vo.first().getAttribute("EndDate");
        System.out.println("condition before if"+endDate.compareTo(startDate));
               if(endDate.compareTo(startDate) >= 0){
               // <
               System.out.println("in if");
               System.out.println("condition is " +endDate.compareTo(startDate));
               System.out.println("add user active is"+addUserActive.getValue());
               addUserActive.setValue("Active");
               addUserActive.setVisible(true);
                   
                   }
               else{
                   System.out.println("in else condition");
                   addUserActive.setValue("InActive");
                   addUserActive.setVisible(true);
                   }
        System.out.println(" project id .. "+evaluateEL("#{bindings.ProjectId.inputValue}"));
        DCBindingContainer bindings =(DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding ob = (OperationBinding)bindings.getOperationBinding("checkProjectName");
        ob.execute();
        
    }

    public void setCreateProjectStartDate(RichInputDate createProjectStartDate) {
        this.createProjectStartDate = createProjectStartDate;
    }

    public RichInputDate getCreateProjectStartDate() {
        return createProjectStartDate;
    }

    public void setCreateProjectEndDate(RichInputDate createProjectEndDate) {
        this.createProjectEndDate = createProjectEndDate;
    }

    public RichInputDate getCreateProjectEndDate() {
        return createProjectEndDate;
    }

    public void setAddUserActive(RichOutputText addUserActive) {
        this.addUserActive = addUserActive;
    }

    public RichOutputText getAddUserActive() {
        return addUserActive;
    }

    public void setAddUserInactive(RichOutputText addUserInactive) {
        this.addUserInactive = addUserInactive;
    }

    public RichOutputText getAddUserInactive() {
        return addUserInactive;
    }


    
}

