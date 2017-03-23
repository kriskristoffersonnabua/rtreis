import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import models.DailyAttendance;
import models.Department;
import models.Employee;
import models.EmploymentStatus;
import models.TableRowModel;

/**
 *
 * @author kc
 */
public class Writer implements Initializable{
    private static Writer instance;
    Font text;
    Font Headers;

    @FXML public TableView<TableRowModel> tableList;
    @FXML public TableColumn<TableRowModel, CheckBox> comboboxColumn;
    @FXML public TableColumn<TableRowModel, String> employeeNameColumn;
    ObservableList<TableRowModel> tableData = FXCollections.observableArrayList();

    @FXML CheckBox allCheck;
    @FXML CheckBox specificCheck;
    @FXML CheckBox departmentCheck;
    @FXML DatePicker startDate;
    @FXML DatePicker endDate;
    @FXML ComboBox<String> department;
    @FXML ProgressBar progbar;

    Employee employee;
    Document document;
    File file;

    class NightDifferential {
        long workinghours;
        long date;
        boolean isRegularHoliday;
        boolean isSpecialHoliday;
        boolean isBothRegularHolidaySpecialHoliday;
        long rh;
        long sh;
        long overtime;

        public long getOvertime() {
            return overtime;
        }

        public void setOvertime(long overtime) {
            this.overtime = overtime;
        }



        public long getWorkinghours() {
            return workinghours;
        }

        public void setWorkinghours(long workinghours) {
            this.workinghours = workinghours;
        }

        public boolean isIsRegularHoliday() {
            return isRegularHoliday;
        }

        public void setIsRegularHoliday(boolean isRegularHoliday) {
            this.isRegularHoliday = isRegularHoliday;
        }

        public boolean isIsSpecialHoliday() {
            return isSpecialHoliday;
        }

        public void setIsSpecialHoliday(boolean isSpecialHoliday) {
            this.isSpecialHoliday = isSpecialHoliday;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public long getRh() {
            return rh;
        }

        public void setRh(long rh) {
            this.rh = rh;
        }

        public long getSh() {
            return sh;
        }

        public void setSh(long sh) {
            this.sh = sh;
        }

    }

    class Holiday {
        long workinghours;
        long date;
        long overtime;
        boolean isSpecial;
        boolean isRegular;
        long sh;
        long rh;

        public long getSh() {
            return sh;
        }

        public void setSh(long sh) {
            this.sh = sh;
        }

        public long getRh() {
            return rh;
        }

        public void setRh(long rh) {
            this.rh = rh;
        }

        public long getOvertime() {
            return overtime;
        }

        public void setOvertime(long overtime) {
            this.overtime = overtime;
        }


        public long getWorkinghours() {
            return workinghours;
        }

        public void setWorkinghours(long workinghours) {
            this.workinghours = workinghours;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public boolean isIsSpecial() {
            return isSpecial;
        }

        public void setIsSpecial(boolean isSpecial) {
            this.isSpecial = isSpecial;
        }

        public boolean isIsRegular() {
            return isRegular;
        }

        public void setIsRegular(boolean isRegular) {
            this.isRegular = isRegular;
        }


    }

    synchronized public void getTimecard(int id) {
        try {

            long totalLates=0; 
            long totalUndertime=0;
            long totalOvertime=0;
            int workingdays = 0;
            boolean notito = false;
            Collection<NightDifferential> nd = new ArrayList<>();
            Collection<Holiday> holidays = new ArrayList<>();

            if(BridgeUnit.getInstance().getRunningSession().isOpen()) BridgeUnit.getInstance().closeSession();
            employee = BridgeUnit.getInstance().getEmployee(id);

            Paragraph employeeName = new Paragraph(employee.getFirst_name()+" "+employee.getLast_name(),Headers);
            Paragraph employeeName2 = new Paragraph(employee.getFirst_name()+" "+employee.getLast_name(),Headers);
            Paragraph departmentName = new Paragraph("Department",text);

            Collection<EmploymentStatus> er =  employee.getEmployment_records();
            for (Iterator<EmploymentStatus> iterator = er.iterator(); iterator.hasNext();) {
                EmploymentStatus next = iterator.next();
                if(next.isIsActive()){
                    departmentName = new Paragraph(next.getDepartment() + " ("+next.getStatus_of_employment()+")",text);
                    break;
                }
            }

            employeeName.setAlignment(Element.ALIGN_CENTER);
            employeeName.setSpacingAfter(2);
            employeeName2.setAlignment(Element.ALIGN_BOTTOM);
            departmentName.setAlignment(Element.ALIGN_CENTER);
            departmentName.setSpacingAfter(5);
            departmentName.setFont(text);

            PdfPTable table;
            table = new PdfPTable(11);
            float[] columnWidths = {2f, 1f, 1f, 1f, 1f, 1f, 1f, 0.5f, 0.5f, 0.5f, 2f};
            table.setWidths(columnWidths);
            table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

            table.addCell(new PdfPCell(new Paragraph("Date",text)));
            table.addCell(new PdfPCell(new Paragraph("Schedule",text)));
            table.addCell(new PdfPCell(new Paragraph("Daytype",text)));
            table.addCell(new PdfPCell(new Paragraph("Time-in",text)));
            table.addCell(new PdfPCell(new Paragraph("Break",text)));
            table.addCell(new PdfPCell(new Paragraph("Resume",text)));
            table.addCell(new PdfPCell(new Paragraph("Time-out",text)));
            table.addCell(new PdfPCell(new Paragraph("Late",text)));
            table.addCell(new PdfPCell(new Paragraph("Short",text)));
            table.addCell(new PdfPCell(new Paragraph("Overtime",text)));
            table.addCell(new PdfPCell(new Paragraph("Remarks",text)));
            List records = (List) employee.getAttendance_record();
            LocalDate s = startDate.getValue();
            LocalDate e = endDate.getValue();
            Calendar startingDate = new GregorianCalendar(s.getYear(),s.getMonthValue()-1,s.getDayOfMonth());
            Calendar endingDate = new GregorianCalendar(e.getYear(),e.getMonthValue()-1,e.getDayOfMonth());
            startingDate.setFirstDayOfWeek(Calendar.MONDAY);

            while(startingDate.compareTo(endingDate)!=1){
                for (ListIterator iterator = records.listIterator(); iterator.hasNext();) {
                    DailyAttendance next = (DailyAttendance) iterator.next();
                    DailyAttendance tomorrow = null;
                    if(iterator.hasNext()){
                        tomorrow = (DailyAttendance) iterator.next();
                        iterator.previous();
                    }
                    if(tomorrow!=null)
                        if(startingDate.getTimeInMillis()==next.date){
                            table.addCell(new PdfPCell(new Paragraph(next.getDate(),text)));
                            table.addCell(new PdfPCell(new Paragraph(next.getSchedule(),text)));
                            table.addCell(new PdfPCell(new Paragraph(next.getDayType(),text)));
                            table.addCell(new PdfPCell(new Paragraph(next.getTimeIn(),text)));
                            table.addCell(new PdfPCell(new Paragraph(next.getBrk(),text)));
                            table.addCell(new PdfPCell(new Paragraph(next.getResume(),text)));
                            table.addCell(new PdfPCell(new Paragraph(next.getTimeOut(),text)));
                            table.addCell(new PdfPCell(new Paragraph(next.getLateIn(),text)));
                            table.addCell(new PdfPCell(new Paragraph(next.getUnderTime(),text)));
                            table.addCell(new PdfPCell(new Paragraph(next.getOvertime(),text)));

                            totalLates += next.lateIn;
                            totalUndertime += next.underTime;
                            totalOvertime += next.overtime;
                            if(next.timeIn!=0&&next.timeOut!=0&&!next.dayType.equalsIgnoreCase("restday")){
                                workingdays+=1;
                                table.addCell(new PdfPCell(new Paragraph(next.getRemarks(),text)));
                            }
                            else if(next.timeIn==0||next.timeOut==0){
                                table.addCell(new PdfPCell(new Paragraph(next.getRemarks()+", no timein/timeout",text)));
                                notito = true;
                            }

                            if(next.isNightDiff){
                                if(notito) continue;
                                NightDifferential n = new NightDifferential();
                                n.setDate(next.date);
                                if(next.overtime!=0){
                                    n.setOvertime(next.overtime);
                                }
                                n.setWorkinghours(next.timeOut-next.timeIn);
                                if(next.isRegularHoliday){
                                    n.setIsRegularHoliday(true);
                                    Date ndstart = new Date(next.getDate()+" 22:15");
                                    Date ndend = new Date(new Date(next.getDate()+" 06:00").getTime()+86400000);
                                    Date clockin = new Date(next.timeIn);
                                    if(next.timeIn!=0&&next.timeOut!=0){
                                        if(clockin.getHours()>=22){
                                            n.setRh((next.date+86400000)-next.timeIn);
                                            if(next.timeOut<=ndend.getTime()){
                                                if(tomorrow!=null && (tomorrow.isRegularHoliday || tomorrow.isSpecialHoliday) ){
                                                    if(tomorrow.isRegularHoliday){
                                                        n.setRh(n.getRh()+(next.timeOut-(next.date+86400000)));
                                                        n.setWorkinghours(0);
                                                    }
                                                    else if(tomorrow.isSpecialHoliday){
                                                        n.setSh(next.timeOut-(next.date+86400000));
                                                        n.setWorkinghours(0);
                                                    }
                                                    else n.setWorkinghours(next.timeOut-(next.date+86400000));
                                                }
                                                else n.setWorkinghours(next.timeOut-(next.date+86400000));
                                            }
                                            else{
                                                if(tomorrow!=null && (tomorrow.isRegularHoliday || tomorrow.isSpecialHoliday) ){
                                                    if(tomorrow.isRegularHoliday){
                                                        n.setRh(n.getRh()+(ndend.getTime()-(next.date+86400000)));
                                                        n.setWorkinghours(0);
                                                    }
                                                    else if(tomorrow.isSpecialHoliday){
                                                        n.setSh(ndend.getTime()-(next.date+86400000));
                                                        n.setWorkinghours(0);
                                                    }
                                                    else n.setWorkinghours(ndend.getTime()-clockin.getTime());
                                                }
                                                else n.setWorkinghours(ndend.getTime()-clockin.getTime());
                                            }
                                        }
                                        if(clockin.getHours()<22){
                                            n.setRh(7200000);
                                            if(next.timeOut<=ndend.getTime()){
                                                if(tomorrow!=null&& (tomorrow.isRegularHoliday || tomorrow.isSpecialHoliday)){
                                                    if(tomorrow.isRegularHoliday){
                                                        n.setRh(n.getRh()+(next.timeOut-(next.date+86400000)));
                                                        n.setWorkinghours(0);
                                                    }
                                                    else if(tomorrow.isSpecialHoliday){
                                                        n.setSh(next.timeOut-(next.date+86400000));
                                                        n.setWorkinghours(0);
                                                    }
                                                    else n.setWorkinghours(next.timeOut-(next.date+86400000));
                                                }
                                                else n.setWorkinghours(next.timeOut-(next.date+86400000));
                                            }
                                            else{
                                                if(tomorrow!=null && (tomorrow.isRegularHoliday || tomorrow.isSpecialHoliday) ){
                                                    if(tomorrow.isRegularHoliday){
                                                        n.setRh(21600000);
                                                        n.setWorkinghours(0);
                                                    }
                                                    else if(tomorrow.isSpecialHoliday){
                                                        n.setSh(21600000);
                                                        n.setWorkinghours(0);
                                                    }
                                                    else n.setWorkinghours(21600000);
                                                }
                                                else n.setWorkinghours(21600000);
                                            }
                                        }
                                    }
                                }
                                else if(next.isSpecialHoliday){
                                    n.setIsSpecialHoliday(true);
                                    Date ndstart = new Date(next.getDate()+" 22:15");
                                    Date ndend = new Date(new Date(next.getDate()+" 06:00").getTime()+86400000);
                                    Date clockin = new Date(next.timeIn);
                                    if(next.timeIn!=0&&next.timeOut!=0){
                                        if(clockin.getHours()>=22){
                                            n.setSh((next.date+86400000)-next.timeIn);
                                            if(next.timeOut<=ndend.getTime()){
                                                if(tomorrow!=null&& (tomorrow.isRegularHoliday || tomorrow.isSpecialHoliday)){
                                                    if(tomorrow.isSpecialHoliday){
                                                        n.setSh(n.getSh()+(next.timeOut-(next.date+86400000)));
                                                        n.setWorkinghours(0);
                                                    }
                                                    else if(tomorrow.isRegularHoliday){
                                                        n.setRh(next.timeOut-(next.date+86400000));
                                                        n.setWorkinghours(0);
                                                    }
                                                    else n.setWorkinghours(next.timeOut-(next.date+86400000));
                                                }
                                                else n.setWorkinghours(next.timeOut-(next.date+86400000));
                                            }
                                            else{
                                                if(tomorrow!=null && (tomorrow.isRegularHoliday || tomorrow.isSpecialHoliday) ){
                                                    if(tomorrow.isRegularHoliday){
                                                        n.setRh(21600000);
                                                        n.setWorkinghours(0);
                                                    }
                                                    else if(tomorrow.isSpecialHoliday){
                                                        n.setSh(21600000);
                                                        n.setWorkinghours(0);
                                                    }
                                                    else n.setWorkinghours(21600000);
                                                }
                                                else n.setWorkinghours(21600000);
                                            }
                                        }
                                        if(clockin.getHours()<22){
                                            n.setSh(7200000);
                                            if(next.timeOut<=ndend.getTime()){
                                                if(tomorrow!=null&& (tomorrow.isRegularHoliday || tomorrow.isSpecialHoliday)){
                                                    if(tomorrow.isSpecialHoliday){
                                                        n.setSh(n.getSh()+(next.timeOut-(next.date+86400000)));
                                                        n.setWorkinghours(0);
                                                    }
                                                    else if(tomorrow.isRegularHoliday){
                                                        n.setRh(next.timeOut-(next.date+86400000));
                                                        n.setWorkinghours(0);
                                                    }
                                                    else n.setWorkinghours(next.timeOut-(next.date+86400000));
                                                }
                                                else n.setWorkinghours(next.timeOut-(next.date+86400000));
                                            }
                                            else{
                                                if(tomorrow!=null && (tomorrow.isRegularHoliday || tomorrow.isSpecialHoliday) ){
                                                    if(tomorrow.isRegularHoliday){
                                                        n.setRh(21600000);
                                                        n.setWorkinghours(0);
                                                    }
                                                    else if(tomorrow.isSpecialHoliday){
                                                        n.setSh(21600000);
                                                        n.setWorkinghours(0);
                                                    }
                                                    else n.setWorkinghours(21600000);
                                                }
                                                else n.setWorkinghours(21600000);
                                            }
                                        }
                                    }
                                }
                                // check again
                                else{
                                    Date ndstart = new Date(next.getDate()+" 22:15");
                                    Date ndend = new Date(new Date(next.getDate()+" 06:00").getTime()+86400000);
                                    Date clockin = new Date(next.timeIn);
                                    if(clockin.getHours()>=22){
                                        if(next.timeOut<=ndend.getTime()){
                                            if(tomorrow!=null&& (tomorrow.isRegularHoliday || tomorrow.isSpecialHoliday)){
                                                if(tomorrow.isSpecialHoliday){
                                                    n.setSh(((next.date+86400000)-next.timeOut));
                                                    n.setWorkinghours(ndstart.getTime()-(next.date+86400000));
                                                }
                                                else if(tomorrow.isRegularHoliday){
                                                    n.setRh(((next.date+86400000)-next.timeOut));
                                                    n.setWorkinghours(ndstart.getTime()-(next.date+86400000));
                                                }
                                            }
                                            else n.setWorkinghours(clockin.getTime()-next.timeOut);
                                        }
                                        else{
                                            if(tomorrow!=null && (tomorrow.isRegularHoliday || tomorrow.isSpecialHoliday) ){
                                                if(tomorrow.isRegularHoliday){
                                                    n.setRh(21600000);
                                                    n.setWorkinghours(ndstart.getTime()-(next.date+86400000));
                                                }
                                                else if(tomorrow.isSpecialHoliday){
                                                    n.setSh(21600000);
                                                    n.setWorkinghours(ndstart.getTime()-(next.date+86400000));
                                                }
                                            }
                                            else{
                                                if(next.timeOut>ndend.getTime()){
                                                    n.setWorkinghours(ndend.getTime()-clockin.getTime());
                                                }
                                                else n.setWorkinghours(next.timeOut-clockin.getTime());
                                            } 
                                        }
                                    }
                                    if(clockin.getHours()<22){
                                        if(next.timeOut<=ndend.getTime()){
                                            if(tomorrow!=null&& (tomorrow.isRegularHoliday || tomorrow.isSpecialHoliday)){
                                                if(tomorrow.isSpecialHoliday){
                                                    n.setSh(((next.date+86400000)-next.timeOut));
                                                    n.setWorkinghours(ndstart.getTime()-(next.date+86400000));
                                                }
                                                else if(tomorrow.isRegularHoliday){
                                                    n.setRh(((next.date+86400000)-next.timeOut));
                                                    n.setWorkinghours(ndstart.getTime()-(next.date+86400000));
                                                }
                                            }
                                            // else n.setWorkinghours(ndstart.getTime()-next.timeOut);
                                            else n.setWorkinghours(next.timeOut - ndstart.getTime());
                                        }
                                        else{
                                            if(tomorrow!=null&& (tomorrow.isRegularHoliday || tomorrow.isSpecialHoliday)){
                                                if(tomorrow.isSpecialHoliday){
                                                    n.setSh((21600000));
                                                    n.setWorkinghours(ndstart.getTime()-(next.date+86400000));
                                                }
                                                else if(tomorrow.isRegularHoliday){
                                                    n.setRh(21600000);
                                                    n.setWorkinghours(ndstart.getTime()-(next.date+86400000));
                                                }
                                            }
                                            else n.setWorkinghours(28800000);
                                        }
                                    }
                                }
                                nd.add(n);
                            }
                            else if(next.isRegularHoliday || next.isSpecialHoliday){
                                if(notito) continue;
                                Holiday h = new Holiday();
                                h.setDate(next.date);
                                h.setWorkinghours(next.timeOut-next.timeIn);
                                if(next.isRegularHoliday){
                                    h.setIsRegular(true);
                                    if(next.timeIn!=0&&next.timeOut!=0){
                                        if(next.brk==0&&next.resume==0){
                                            if((next.timeOut-next.timeIn)>28800000)
                                                h.setRh(28800000);
                                            else h.setRh(next.timeOut-next.timeIn);
                                        }
                                        else if(next.brk!=0&&next.resume!=0){
                                            long total = (next.brk-next.timeIn) + (next.timeOut-next.resume);
                                            if(total>28800000) h.setRh(28800000);
                                            else h.setRh(total);
                                        }
                                        else{
                                            if((next.timeOut-next.timeIn)>28800000)
                                                h.setRh(28800000);
                                            else h.setRh(next.timeOut-next.timeIn);
                                        }
                                    }
                                    if(next.overtime!=0){
                                        h.setOvertime(next.overtime);
                                    }
                                }
                                else if(next.isSpecialHoliday){
                                    h.setIsRegular(true);
                                    if(next.timeIn!=0&&next.timeOut!=0){
                                        if(next.brk==0&&next.resume==0){
                                            if((next.timeOut-next.timeIn)>28800000)
                                                h.setSh(28800000);
                                            else h.setSh(next.timeOut-next.timeIn);
                                        }
                                        else if(next.brk!=0&&next.resume!=0){
                                            long total = (next.brk-next.timeIn) + (next.timeOut-next.resume);
                                            if(total>28800000) h.setSh(28800000);
                                            else h.setSh(total);
                                        }
                                        else{
                                            if((next.timeOut-next.timeIn)>28800000)
                                                h.setRh(28800000);
                                            else h.setRh(next.timeOut-next.timeIn);
                                        }
                                    }
                                    if(next.overtime!=0){
                                        h.setOvertime(next.overtime);
                                    }
                                }
                                holidays.add(h);
                            }
                        }
                }
                startingDate.add(Calendar.DAY_OF_YEAR, 1);
            }

            int h = 0;
            int m = (int) Math.floor(totalLates/60000);
            if(m>=60){
                h += m/60;
                m -= (m/60)*60;
            }
            String hours = h+"";
            String minutes = m+"";
            String lates = totalLates == 0 ? "0" :hours+"."+minutes;

            h = 0;
            m = (int) Math.floor(totalUndertime/60000);
            if(m>=60){
                h += m/60;
                m -= (m/60)*60;
            }
            hours = h+"";
            minutes = m+"";
            String under = totalUndertime == 0 ? "0" :hours+"."+minutes;

            h = 0;
            m = (int) Math.floor(totalOvertime/60000);
            if(m>=60){
                h += m/60;
                m -= (m/60)*60;
            }
            hours = h+"";
            minutes = m+"";
            String ot = totalOvertime == 0 ? "0" :hours+"."+minutes;

            Chunk workdays = new Chunk("                                      total days: "+workingdays,text);
            Chunk late = new Chunk("  total late: "+lates,text);
            Chunk underTime = new Chunk("  total undertime: "+under,text);
            Chunk overtime = new Chunk("  total overtime: "+ot,text);

            PdfPTable nightdiffs = new PdfPTable(5);
            nightdiffs.setHorizontalAlignment(Element.ALIGN_LEFT);
            nightdiffs.setWidthPercentage(40);

            PdfPCell date = new PdfPCell(new Paragraph("Date",text));
            PdfPCell hrs = new PdfPCell(new Paragraph("Hours",text));
            PdfPCell shhours = new PdfPCell(new Paragraph("Special Holiday",text));
            PdfPCell rhhours = new PdfPCell(new Paragraph("Regular Holiday",text));
            PdfPCell ovrtime = new PdfPCell(new Paragraph("Overtime",text));

            nightdiffs.addCell(date);
            nightdiffs.addCell(hrs);
            nightdiffs.addCell(shhours);
            nightdiffs.addCell(rhhours);
            nightdiffs.addCell(ovrtime);

            float[] columnWidth = {2f, 2f, 2.5f, 2.5f, 2f};
            nightdiffs.setWidths(columnWidth);

            for (Iterator iterator = nd.iterator(); iterator.hasNext();) {
                NightDifferential next = (NightDifferential) iterator.next();

                SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/YYYY");
                String d = next.getDate() == 0 ? "" :dateFormatter.format(new Date(next.getDate()));

                h = 0;
                m = (int) Math.floor(next.getWorkinghours()/60000);
                if(m>=60){
                    h += m/60;
                    m -= (m/60)*60;
                }
                hours = h+"";
                minutes = m+"";
                String workhours = next.getWorkinghours() == 0 ? "" :hours+"."+minutes;

                h = 0;
                m = (int) Math.floor(next.sh/60000);
                if(m>=60){
                    h += m/60;
                    m -= (m/60)*60;
                }
                hours = h+"";
                minutes = m+"";
                String sh = next.sh == 0 ? "" :hours+"."+minutes;

                h = 0;
                m = (int) Math.floor(next.rh/60000);
                if(m>=60){
                    h += m/60;
                    m -= (m/60)*60;
                }
                hours = h+"";
                minutes = m+"";
                String rh = next.rh == 0 ? "" :hours+"."+minutes;

                h = 0;
                m = (int) Math.floor(next.overtime/60000);
                if(m>=60){
                    h += m/60;
                    m -= (m/60)*60;
                }
                hours = h+"";
                minutes = m+"";
                String o = next.overtime == 0 ? "" :hours+"."+minutes;

                nightdiffs.addCell(new PdfPCell(new Paragraph(d,text)));
                nightdiffs.addCell(new PdfPCell(new Paragraph(workhours,text)));
                nightdiffs.addCell(new PdfPCell(new Paragraph(sh,text)));
                nightdiffs.addCell(new PdfPCell(new Paragraph(rh,text)));
                nightdiffs.addCell(new PdfPCell(new Paragraph(o,text)));
            }

            PdfPTable holidaylists = new PdfPTable(4);
            holidaylists.setHorizontalAlignment(Element.ALIGN_RIGHT);
            holidaylists.setSpacingBefore(0);
            holidaylists.setWidthPercentage(40);

            float[] columnW = {2f, 2f, 2.5f, 2.5f};
            holidaylists.setWidths(columnW);

            Paragraph p1 = new Paragraph("HOLIDAYS           ",Headers);
            p1.setLeading(-20);
            p1.setAlignment(Element.ALIGN_RIGHT);
            p1.setSpacingAfter(8);

            Paragraph p2 = new Paragraph("        NIGHT DIFFERENTIALS",Headers);
            p2.setSpacingAfter(8);

            date = new PdfPCell(new Paragraph("Date",text));
            hrs = new PdfPCell(new Paragraph("Overtime",text));
            shhours = new PdfPCell(new Paragraph("Special Holiday",text));
            rhhours = new PdfPCell(new Paragraph("Regular Holiday",text));
            holidaylists.addCell(date);
            holidaylists.addCell(hrs);
            holidaylists.addCell(shhours);
            holidaylists.addCell(rhhours);

            for (Iterator iterator = holidays.iterator(); iterator.hasNext();) {
                Holiday next = (Holiday) iterator.next();

                SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/YYYY");
                String d = next.getDate() == 0 ? "" :dateFormatter.format(new Date(next.getDate()));

                h = 0;
                m = (int) Math.floor(next.overtime/60000);
                if(m>=60){
                    h += m/60;
                    m -= (m/60)*60;
                }
                hours = h+"";
                minutes = m+"";
                String workhours = next.overtime == 0 ? "" :hours+"."+minutes;

                h = 0;
                m = (int) Math.floor(next.sh/60000);
                if(m>=60){
                    h += m/60;
                    m -= (m/60)*60;
                }
                hours = h+"";
                minutes = m+"";
                String sh = next.sh == 0 ? "" :hours+"."+minutes;

                h = 0;
                m = (int) Math.floor(next.rh/60000);
                if(m>=60){
                    h += m/60;
                    m -= (m/60)*60;
                }
                hours = h+"";
                minutes = m+"";
                String rh = next.rh == 0 ? "" :hours+"."+minutes;

                holidaylists.addCell(new PdfPCell(new Paragraph(d,text)));
                holidaylists.addCell(new PdfPCell(new Paragraph(workhours,text)));
                holidaylists.addCell(new PdfPCell(new Paragraph(sh,text)));
                holidaylists.addCell(new PdfPCell(new Paragraph(rh,text)));
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm");
            String created = "Timecard ("+dateFormat.format(new Date())+")";
            document.setHeader(new HeaderFooter(new Phrase(created), false));

            document.add(employeeName);
            document.add(departmentName);
            document.add(table);
            document.add(workdays);
            document.add(late);
            document.add(underTime);
            document.add(overtime);
            document.add(p2);
            document.add(nightdiffs);
            document.add(p1);
            document.add(holidaylists);
            document.add(employeeName2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateReport(){
        if(startDate.getValue()==null){
            JOptionPane.showMessageDialog(null, "Starting date must not be empty.");
            return;
        }
        if(endDate.getValue()==null){
            JOptionPane.showMessageDialog(null, "End date must not be empty.");
            return;
        }

        progbar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        pdfGeneratorWoker();
    }

    public void pdfGeneratorWoker () {
        Service<Void> backgroundThread = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        if(specificCheck.isSelected()){
                            try {
                                file = new File(System.getProperty("user.home")+System.getProperty("file.separator")+"Documents"+System.getProperty("file.separator")+"RTRHOSPITAL");

                                if(file.isDirectory() && file.exists()){ 
                                    file = new File(System.getProperty("user.home")+System.getProperty("file.separator")+"Documents"+System.getProperty("file.separator")+"RTRHOSPITAL"+System.getProperty("file.separator")+"report.pdf");
                                }
                                else{
                                    new File(System.getProperty("user.home")+System.getProperty("file.separator")+"Documents"+System.getProperty("file.separator")+"RTRHOSPITAL").mkdir();
                                    file = new File(System.getProperty("user.home")+System.getProperty("file.separator")+"Documents"+System.getProperty("file.separator")+"RTRHOSPITAL"+System.getProperty("file.separator")+"report.pdf");
                                }

                                file.createNewFile();
                                document = new Document(PageSize.A4.rotate());
                                PdfWriter.getInstance(document,new FileOutputStream(file));
                                document.open(); 
                                for (Iterator<TableRowModel> iterator = tableData.iterator(); iterator.hasNext();) {
                                    TableRowModel next = iterator.next();
                                    if(next.getCheckbox().isSelected()){
                                        getTimecard(next.getId());
                                        if(iterator.hasNext())
                                        document.newPage();
                                    }
                                }
                                document.close();
                            } catch (Exception ex) {
                                Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        else if(allCheck.isSelected() && !departmentCheck.isSelected()){
                            try {
                                file = new File(System.getProperty("user.home")+System.getProperty("file.separator")+"Documents"+System.getProperty("file.separator")+"RTRHOSPITAL");

                                if(file.isDirectory() && file.exists()){ 
                                    file = new File(System.getProperty("user.home")+System.getProperty("file.separator")+"Documents"+System.getProperty("file.separator")+"RTRHOSPITAL"+System.getProperty("file.separator")+"report.pdf");
                                }
                                else{
                                    new File(System.getProperty("user.home")+System.getProperty("file.separator")+"Documents").mkdir();
                                    file = new File(System.getProperty("user.home")+System.getProperty("file.separator")+"Documents"+System.getProperty("file.separator")+"RTRHOSPITAL"+System.getProperty("file.separator")+"report.pdf");
                                }

                                file.createNewFile();
                                document = new Document(PageSize.A4.rotate());
                                PdfWriter.getInstance(document,new FileOutputStream(file));
                                document.open(); 
                                for (Iterator<TableRowModel> iterator = tableData.iterator(); iterator.hasNext();) {
                                    TableRowModel next = iterator.next();
                                    if(next.getCheckbox().isSelected()){
                                        getTimecard(next.getId());
                                        if(iterator.hasNext())
                                            document.newPage();
                                    }
                                }
                                document.close();
                            } catch (Exception ex) {
                                Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        else if(departmentCheck.isSelected()){
                            try {
                                file = new File(System.getProperty("user.home")+"/Documents/RTRHOSPITAL");

                                if(file.isDirectory() && file.exists()){ 
                                    file = new File(System.getProperty("user.home")+"/Documents/RTRHOSPITAL/report.pdf");
                                }
                                else{
                                    new File(System.getProperty("user.home")+"/Documents").mkdir();
                                    file = new File(System.getProperty("user.home")+"/Documents/RTRHOSPITAL/report.pdf");
                                }
                                file.createNewFile();
                                document = new Document(PageSize.A4.rotate());
                                PdfWriter.getInstance(document,new FileOutputStream(file));
                                document.open(); 
                                for (Iterator<TableRowModel> iterator = tableData.iterator(); iterator.hasNext();) {
                                    TableRowModel next = iterator.next();
                                    if(next.getCheckbox().isSelected()){
                                        employee = BridgeUnit.getInstance().getEmployee(next.getId());
                                        Collection<EmploymentStatus> er =  employee.getEmployment_records();
                                        for (Iterator<EmploymentStatus> iterator1 = er.iterator(); iterator1.hasNext();) {
                                            EmploymentStatus next1 = iterator1.next();
                                            if(next1.isIsActive()){
                                                if(next1.getDepartment().equalsIgnoreCase(department.getSelectionModel().getSelectedItem().trim())){
                                                    getTimecard(next.getId()); 
                                                    break;
                                                }

                                            }
                                        }
                                        if(iterator.hasNext())
                                            document.newPage();
                                    }
                                }
                                document.close();
                            } catch (Exception ex) {
                                Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }            
                        return null;
                    }
                };
            }
        };
        backgroundThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                progbar.setProgress(0);
                JOptionPane.showMessageDialog(null, "Report generated. File is located in documents inside RTR folder. :)");
            }
        });
        backgroundThread.restart();
    }

    public static Writer getInstance() {
        return instance;
    }

    public void allSelected () {
        if(allCheck.isSelected())specificCheck.setSelected(false);

        for (Iterator<TableRowModel> iterator = tableData.iterator(); iterator.hasNext();) {
            TableRowModel next = iterator.next();
            if(!next.getCheckbox().isSelected()) next.getCheckbox().setSelected(true);
        }
    }
    public void specificSelected () {
        if(specificCheck.isSelected()){
            if(specificCheck.isSelected())allCheck.setSelected(false);

            for (Iterator<TableRowModel> iterator = tableData.iterator(); iterator.hasNext();) {
                TableRowModel next = iterator.next();
                if(next.getCheckbox().isSelected()) next.getCheckbox().setSelected(false);
            }
            departmentCheck.setSelected(false);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        ObservableList<String> dept= FXCollections.observableArrayList();
        List dList = BridgeUnit.getInstance().getAllDepartments();
        for (Iterator iterator = dList.iterator(); iterator.hasNext();) {
            Department next = (Department) iterator.next();
            dept.add(next.getDepartment_name());
        }
        department.setItems(dept);

        text = new Font(BaseFont.FONT_TYPE_T1, (float) 8.6);
        text.setColor(Color.black);
        Headers = new Font(BaseFont.FONT_TYPE_T1, (float) 9.0);
        Headers.setColor(Color.black);
        Headers.setStyle(Font.BOLD);


        comboboxColumn.setCellValueFactory(new PropertyValueFactory<TableRowModel, CheckBox>("checkbox"));
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<TableRowModel, String>("name"));
        tableList.setItems(tableData);

        refreshList();
    }

    public void refreshList () {
        List<Employee> list = BridgeUnit.getInstance().getAllEmployees();
        tableData.clear();
        for (Iterator<Employee> iterator = list.iterator(); iterator.hasNext();) {
            Employee next = iterator.next();
            TableRowModel data = new TableRowModel(next.getLast_name()+", "+next.getFirst_name());
            data.setId(next.getBiometrics_id());
            tableData.add(data);
        }
    }
}
