/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package clientmanager;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 *
 * @author Francis King C. Uyguangco
 * BSIT 2 ITCC 11.1 A
 * 
 */
class SQLAccess 
{
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement newClient = null, updateClient = null, insertServiceData = null, deleteClient = null, deleteInvoiceClient = null,
            deleteClientInvoice = null, deleteClientServices = null, newService = null, checkClient = null, checkService = null, deleteService = null,
            updateService = null, insertInvoiceClient = null, insertInvoice = null, checkInvoice = null, updateInvoiceClient = null, deleteInvoice = null;
    private ResultSet ServicesList = null, ClientList = null, InvoiceList = null, InvoiceClientList = null, ClientServicesList = null;
    Scanner cl = new Scanner(System.in);
    Scanner c = new Scanner(System.in);
    SimpleDateFormat dft = new SimpleDateFormat("yyyy-mm-dd");
    
    public void readDataBase() throws Exception 
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            connect = DriverManager.getConnection("jdbc:mysql://localhost/vaclientmanager","root","1234");
            statement = connect.createStatement();  
            boolean loop = true;
            do
            {
                System.out.println("==============================\nWelcome to VAClientManager\n==============================\n"
                        + "What can I help you with?\n1.Add New Client\n2.Existing Client\n3.Services Offered\n"
                        + "4.Invoice\n5.Others\n6.Exit\n==============================");
                int choice = cl.nextInt();
                switch (choice)
                {
                    case 1:
                    {
                        AddNewClient();
                        break;
                    }
                    case 2:
                    {
                        ExistingClient();
                        break;
                    }
                    case 3:
                    {
                        ServiceList();
                        break;
                    }
                    case 4:
                    {
                        InvoiceList();
                        break;
                    }
                    case 5:
                    {
                        Others();
                        break;
                    }
                    case 6:
                    {
                        System.out.println("Exiting program....\nHave a Great Day!");
                        System.exit(0);
                    }
                    default:
                        System.out.println("Choose between 1-6 only.");
                }
            }
            while (loop == true);
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            closeResources();
        }
    }
    
    private String getServiceAvailed(String serviceID) throws SQLException
    {
        try 
        {
            ServicesList = connect.createStatement().executeQuery("select * from vaclientmanager.services");
            String Description = null;
            while (ServicesList.next()) 
            {
                String ID = ServicesList.getString("serviceID");
                Description = ServicesList.getString("sDescription");
                String Rate = ServicesList.getString("rate");
                if (ID.equals(serviceID)) 
                {
                    break;
                }
            }
            return Description;
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            return null;
        }
    }
    
    private int getRate(String serviceID)
    {
        try 
        {
            int Rate = 0;
            ServicesList = connect.createStatement().executeQuery("select rate from vaclientmanager.services where serviceID = " + serviceID);
            while (ServicesList.next()) 
            {
                Rate = ServicesList.getInt("rate");
            }
            return Rate;
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            return -1;
        }
    }
    
    private int ViewServices() throws SQLException
    {
        try 
        {
            int counter = 1;
            ServicesList = connect.createStatement().executeQuery("select * from vaclientmanager.services");
            while (ServicesList.next()) 
            {
                counter++;
            }
            return counter + 100;
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            return -1;
        }
    }  
    
    private String getInvoiceNumForInvoice(String id)
    {
        try 
        {
            InvoiceList = connect.createStatement().executeQuery("select * from vaclientmanager.invoice");
            while (InvoiceList.next()) 
            {
                String ID = InvoiceList.getString("invoiceNum");
                String currentDate = InvoiceList.getString("currentDate");
                String clientID = InvoiceList.getString("clientID");
                LocalDate date = LocalDate.now();
                if(String.valueOf(date).equals(currentDate) && id.equals(clientID))
                {
                    return ID;
                }
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
         return "";
    }
    
    private String getInvoiceNum(String id)
    {
        try 
        {
            InvoiceList = connect.createStatement().executeQuery("select * from vaclientmanager.invoice");
            while (InvoiceList.next()) 
            {
                String ID = InvoiceList.getString("invoiceNum");
                String clientID = InvoiceList.getString("clientID");
                if(id.equals(clientID))
                {
                    return ID;
                }
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
         return "";
    }
    
    private void ViewClientList() throws SQLException
    {
        try 
        {
            System.out.println("==============================");
            ClientList = connect.createStatement().executeQuery("select * from vaclientmanager.client");
            while (ClientList.next()) 
            {
                String ID = ClientList.getString("clientID");
                String contact = ClientList.getString("contactNum");
                String FName = ClientList.getString("FName");
                String LName = ClientList.getString("LName");
                String address = ClientList.getString("address");
                System.out.println("Client Number: " + ID + " Contact Number: " + contact + " First Name: " + FName +
                        " Last Name: " + LName + " Address: " + address);
            }
            System.out.println("==============================");
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    
    private void ViewServiceList() throws SQLException
    {
        try 
        {
            System.out.println("==============================\nServices Offered:");
            ServicesList = connect.createStatement().executeQuery("select * from vaclientmanager.services");
            while (ServicesList.next()) 
            {
                String ID = ServicesList.getString("serviceID");
                String Description = ServicesList.getString("sDescription");
                String Rate = ServicesList.getString("rate");
                System.out.println("Service Number: " + ID + " Description: " + Description + " Rate: $" + Rate);
            }
            System.out.println("==============================");
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    
    private void ViewInvoiceList() throws SQLException
    {
        try 
        {
            System.out.println("==============================\nAll Invoice:");
            InvoiceList = connect.createStatement().executeQuery("select * from vaclientmanager.invoice");
            while (InvoiceList.next()) 
            {
                String ID = InvoiceList.getString("invoiceNum");
                String clientID = InvoiceList.getString("clientID");
                String date = InvoiceList.getString("currentDate");
                System.out.println("Invoice Number: " + ID + " Client ID: " + clientID + " Date: " + date);
                ViewInvoiceClientList(ID);
            }
            System.out.println("==============================");
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    
    private void ViewInvoiceClientList(String invoiceNum) throws SQLException
    {
        try 
        {
            int Total = 0;
            InvoiceClientList = connect.createStatement().executeQuery("select * from vaclientmanager.invoice_client");
            while (InvoiceClientList.next()) 
            {
                String id = InvoiceClientList.getString("invoiceNum");
                if (id.equals(invoiceNum)) 
                {
                    String serviceID = InvoiceClientList.getString("serviceID");
                    String description = getServiceAvailed(serviceID);
                    int workHours = InvoiceClientList.getInt("workHours");
                    String accomplishDate = InvoiceClientList.getString("accomplishDate");
                    System.out.println("Service Number: " + serviceID + " Service Availed: " + description + " Work Hours: " + workHours + " Date Accomplished: " + accomplishDate);
                    int Rate = getRate(serviceID);
                    Total = Total + (Rate * workHours);
                }
            }
            System.out.println("Total Income: $" + Total + "\n==============================");
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    
    private void TotalBillperClient() throws SQLException
    {
        try 
        {
            System.out.println("==============================\nTotal Bill per Client:");
            int Total = 0;
            ClientList = connect.createStatement().executeQuery("select * from vaclientmanager.client");
            while (ClientList.next())
            {
                String clientID = ClientList.getString("clientID");
                String FName = ClientList.getString("FName");
                String LName = ClientList.getString("LName");
                InvoiceList = connect.createStatement().executeQuery("select * from vaclientmanager.invoice");
                while (InvoiceList.next())
                {
                    String client = InvoiceList.getString("clientID");
                    String invoiceNum = InvoiceList.getString("invoiceNum");
                    if (clientID.equals(client)) 
                    {
                        InvoiceClientList = connect.createStatement().executeQuery("select * from vaclientmanager.invoice_client");

                        while (InvoiceClientList.next()) 
                        {
                            String id = InvoiceClientList.getString("invoiceNum");
                            if (id.equals(invoiceNum))
                            {
                                String serviceID = InvoiceClientList.getString("serviceID");
                                int workHours = InvoiceClientList.getInt("workHours");
                                int Rate = getRate(serviceID);
                                Total = Total + (Rate * workHours);
                            }
                        }
                    }
                }
                System.out.println("Client Number: " + clientID + " Client Name: " + 
                    LName + ", " + FName + " Total Billing: " + Total);
                Total = 0;
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    
    private void TotalHoursperServices() throws SQLException
    {
        int TotalHours = 0;
        int Total = 0;
        System.out.println("==============================\nTotal Hours per Service:");
        ServicesList = connect.createStatement().executeQuery("select * from vaclientmanager.services");
        while(ServicesList.next())
        {
            String serviceID = ServicesList.getString("serviceID");
            String description = ServicesList.getString("sDescription");
            int rate = ServicesList.getInt("rate");
            InvoiceClientList = connect.createStatement().executeQuery("select * from vaclientmanager.invoice_client");
            while (InvoiceClientList.next())
            {
                String id = InvoiceClientList.getString("serviceID");
                if (id.equals(serviceID))
                {
                    int Hours = InvoiceClientList.getInt("workHours");
                    TotalHours = TotalHours + Hours;
                }
            }
            Total = TotalHours * rate;
            System.out.println("Service Number: " + serviceID + " Service Description: " + 
                    description + " Total Hours: " + TotalHours + " Rate: $" + rate + " Total Billing: " + Total);
                TotalHours = 0;
                Total = 0;
        }
    }
    
    private void AddNewClient() throws SQLException, ParseException 
    {
        try 
        {
            System.out.println("==============================\nAdding New Client\nEnter Required Details");
            System.out.print("Contact Number (09#########): ");
            String contact = c.nextLine();
            checkClient = connect.prepareStatement("Select count(*) from vaclientmanager.client where contactNum = ?");
            checkClient.setString(1, contact);
            ResultSet result = checkClient.executeQuery();
            result.next();
            int count = result.getInt(1);
    
            if (count > 0) 
            {
                System.out.println("Client with the same contact number already exists. Aborting client creation.");
                return;
            }
    
            System.out.print("First Name: ");
            String firstName = c.nextLine();
            System.out.print("Last Name: ");
            String lastName = c.nextLine();
            System.out.print("Address: ");
            String address = c.nextLine();

            newClient = connect.prepareStatement("insert into vaclientmanager.client values (default, ?, ?, ?, ?)");
            newClient.setString(1, contact);
            newClient.setString(2, firstName);
            newClient.setString(3, lastName);
            newClient.setString(4, address);
            newClient.executeUpdate();

            System.out.println("Client added successfully.\n==============================");

            System.out.println("Would you like to add service availed?\n1.Yes\n2.No");
            int Choice = cl.nextInt();
            if (Choice == 1) 
            {
                String ID = null;
                ClientList = connect.createStatement().executeQuery("SELECT clientID FROM vaclientmanager.client where contactNum = " + contact);
                while (ClientList.next()) 
                {
                    ID = ClientList.getString("clientID");
                }
                AddServiceData(ID);
            } 
            else if (Choice == 2) 
            {
                System.out.println("Going back to main menu.");
            }
            else 
            {
                System.out.println("Enter 1 or 2 only.\n==============================");
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    
    private void AddServiceData(String clientID) throws SQLException, ParseException
    {
        try 
        {
            Scanner n = new Scanner(System.in);
            Scanner t = new Scanner(System.in);
            int counter = ViewServices();
            ViewServiceList();
            System.out.print("What service would you like to avail? (Enter Service Number only): ");
            int service = n.nextInt();
            if (service > 100 && service <= counter) 
            {
                String serviceID = String.valueOf(service);
                System.out.print("Request Date(yyyy-mm-dd): ");
                String rDateInput = t.nextLine();
                String rDate = dft.format(dft.parse(rDateInput));
                System.out.print("Deadline(yyyy-mm-dd): ");
                String deadlineInput = t.nextLine();
                String deadline = dft.format(dft.parse(deadlineInput));
                insertServiceData = connect.prepareStatement("insert into vaclientmanager.client_services values (?,?,?,?)");
                insertServiceData.setString(1, clientID);
                insertServiceData.setString(2, serviceID);
                insertServiceData.setString(3, rDate);
                insertServiceData.setString(4, deadline);
                insertServiceData.executeUpdate();

                System.out.println("Client Request has been saved.");
            } 
            else 
            {
                System.out.println("Error Service Number Entry.");
                AddServiceData(clientID);
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        catch (ParseException i)
        {
            System.out.println("Please follow the format (yyyy-mm-dd).");
            AddServiceData(clientID);
        }
    }
    
    private void AddNewService() throws SQLException
    {
        try 
        {
            System.out.println("==============================\nAdding New Service to offer\nEnter Required Details");
            Scanner cl = new Scanner(System.in);
            System.out.print("Service Description: ");
            String sDescription = cl.nextLine();
            
            checkService = connect.prepareStatement("select count(*) from vaclientmanager.services where sDescription = ?");
            checkService.setString(1, sDescription);
            ResultSet result = checkService.executeQuery();
            result.next();
            int count = result.getInt(1);

            if (count > 0) 
            {
                System.out.println("A service with the same description and rate already exists. Aborting service creation.");
                return;
            }

            System.out.print("Rate: $");
            String rate = c.nextLine();
            newService = connect.prepareStatement("INSERT INTO vaclientmanager.services (sDescription, rate) VALUES (?, ?)");
            newService.setString(1, sDescription);
            newService.setString(2, rate);
            newService.executeUpdate();

            System.out.println("Service added successfully\n==============================");
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    
    private void AddServiceInvoice(String invoiceNum, String ID) throws SQLException, ParseException
    {
        try
        {
        int Total = 0;
        System.out.println("==============================\nAll services availed by client: " + ID);
        ClientServicesList = connect.createStatement().executeQuery("select * from vaclientmanager.client_services where clientID = " + ID);
        while (ClientServicesList.next()) 
            {
                String serviceID = ClientServicesList.getString("serviceID");
                String rDateInput = ClientServicesList.getString("requestDate");
                String rDate = dft.format(dft.parse(rDateInput));
                String deadlineInput = ClientServicesList.getString("deadline");
                String deadline = dft.format(dft.parse(deadlineInput));
                System.out.println("Service Number: " + serviceID + " Request Date: " + rDate + " Deadline: " + deadline);
            }
            System.out.println("==============================");
            do 
            {
                System.out.print("Which service would you like to add to the invoice? ");
                String service = c.nextLine();
                System.out.print("Hours of work: ");
                int hours = cl.nextInt();
                System.out.print("Date accomplished (yyyy-mm-dd): ");
                String dateInput = c.nextLine();
                String date = dft.format(dft.parse(dateInput));
                insertInvoiceClient = connect.prepareStatement("insert into vaclientmanager.invoice_client (invoiceNum, serviceID, workHours, accomplishDate) values (?,?,?,?)");
                insertInvoiceClient.setString(1, invoiceNum);
                insertInvoiceClient.setString(2, service);
                insertInvoiceClient.setInt(3, hours);
                insertInvoiceClient.setString(4, date);
                insertInvoiceClient.executeUpdate();
            
                int rate = getRate(service);
                Total += rate * hours;
            
                System.out.println("Service added to invoice.");
                System.out.print("Want to add more? (1.Yes / 2.No): ");
            } 
            while (cl.nextInt() == 1);
            System.out.println("Invoice Number: " + invoiceNum + " Client Number: " + ID);
                System.out.println("Total Income: $" + Total + "\n==============================");
                InvoiceList();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch (ParseException i)
        {
            System.out.println("Please follow the format (yyyy-mm-dd).");
            AddServiceInvoice(invoiceNum, ID);
        }
    }
    
    private void AddData() throws SQLException, ParseException
    {
        ViewClientList();
        System.out.println("Which Client would you like to add a new service request?");
        String clientID = c.nextLine();
        if(ListChecker(clientID))
        {
            AddServiceData(clientID);
        }
        else
        {
            System.out.println("Client Number entered does not exist.\n==============================");
            ExistingClient();
        }
    }
    
    private void GenerateInvoice() throws ParseException
    {
        try
        {
            ViewClientList();
            System.out.print("Enter client ID you want to generate invoice for: ");
            String ID = c.nextLine();
            if(ListChecker(ID))
            {
                 
                insertInvoice = connect.prepareStatement("insert into vaclientmanager.invoice values (default,?,?)", Statement.RETURN_GENERATED_KEYS);
                insertInvoice.setString(1, ID);
                LocalDate date = LocalDate.now();
                insertInvoice.setString(2, String.valueOf(date));
                insertInvoice.executeUpdate();
                String invoiceNum = getInvoiceNumForInvoice(ID);
                AddServiceInvoice(invoiceNum,ID);
            }
            else
            {
                System.out.println("Client number does not exist.\n==============================");
                InvoiceList();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    private void UpdateClientList() throws ParseException
    {
        try 
        {
            ViewClientList();
            System.out.print("Enter the client ID you want to update: ");
            String clientID = c.nextLine();
            if(ListChecker(clientID))
            {
                System.out.println("What do you want to update?\n1.Contact Number\n2.First Name\n3.Last Name\n4.Address");
                int choice = cl.nextInt();

                String dataToUpdate;
                String newValue;

                switch (choice) 
                    {
                        case 1:
                        {
                            dataToUpdate = "contactNum";
                            System.out.print("Enter the new contact number: ");
                            newValue = c.nextLine();
                            break;
                        }
                        case 2:
                        {
                            dataToUpdate = "FName";
                            System.out.print("Enter the new first name: ");
                            newValue = c.nextLine();
                            break;
                        }
                        case 3:
                        {
                            dataToUpdate = "LName";
                            System.out.print("Enter the new last name: ");
                            newValue = c.nextLine();
                            break;
                        }
                        case 4:
                        {
                            dataToUpdate = "address";
                            System.out.print("Enter the new address: ");
                            newValue = c.nextLine();
                            break;
                        }
                        default:
                            System.out.println("Invalid choice");
                            return;
                    }

                updateClient = connect.prepareStatement("UPDATE vaclientmanager.client SET " + dataToUpdate + " = ? WHERE clientID = ?");
                updateClient.setString(1, newValue);
                updateClient.setString(2, clientID);
                updateClient.executeUpdate();

                System.out.println("Client information updated successfully.\n==============================");
            }
            else
            {
                System.out.println("Client Number entered does not exist.\n==============================");
                ExistingClient();
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    
    private void UpdateService()
    {
        try
        {
            ViewServiceList();
            System.out.print("Enter the ServiceID of the service you want to update: ");
            int serviceID = cl.nextInt();
        
            boolean serviceExists = checkServiceExists(serviceID);
            if (!serviceExists) 
            {
                System.out.println("ServiceID does not exist.\n==============================");
                ServiceList();
            }
            else
            {
                System.out.println("What would you like to update from this service?\n1.Description\n2.Rate");
                int choice = cl.nextInt();
            
                String dataToUpdate;
                String newValue;
                switch (choice)
                {
                    case 1:
                    {
                        dataToUpdate = "sDescription";
                        System.out.print("Enter the new description: ");
                        newValue = c.nextLine();
                        break;
                    }
                    case 2:
                    {
                        dataToUpdate = "rate";
                        System.out.print("Enter the new rate: $");
                        newValue = c.nextLine();
                        break;
                    }
                    default:
                        System.out.println("Invalid choice");
                        return;
                }
                updateService = connect.prepareStatement("update vaclientmanager.services set " + dataToUpdate + " = " + newValue + " where serviceID =" + serviceID);
                updateService.executeUpdate();

                System.out.println("Client information updated successfully.\n==============================");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    private void EditInvoice() throws ParseException
    {
        try 
        {
            ViewInvoiceList();
            System.out.print("Enter Invoice Number you wish to Edit: ");
            String invoiceNum = c.nextLine();
        
            if (InvoiceChecker(invoiceNum)) 
            {
                String clientID = getClientIDFromInvoice(invoiceNum);
                boolean validChoice = false;
                while (!validChoice) 
                {
                    System.out.println("What would you like to do?\n1.Add Service\n2.Update Hours\n3.Exit");
                    int choice = cl.nextInt();
                    switch (choice) 
                    {
                        case 1:
                        {
                            AddServiceInvoice(invoiceNum,clientID);
                            validChoice = true;
                            break;
                        }
                        case 2:
                        {
                            UpdateServiceHours(invoiceNum);
                            validChoice = true; 
                            break;
                        }
                        case 3:
                        {
                            return;
                        }
                        default:
                            System.out.println("Invalid choice.");
                    }
                }
                System.out.println("Invoice Updated Successfully.\n==============================");
            }   
            else 
            {
                System.out.println("Invoice number does not exist.\n==============================");
                InvoiceList();
            }
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    
    private void DeleteService()
    {
        try 
        {
            ViewServiceList();
            System.out.print("Enter the ServiceID you want to delete: ");
            int serviceID = cl.nextInt();
        
            boolean serviceExists = checkServiceExists(serviceID);
            if (!serviceExists) 
            {
                System.out.println("ServiceID does not exist.\n==============================");
                ServiceList();
            }
            else
            {
                deleteClientServices = connect.prepareStatement("delete from vaclientmanager.client_services where serviceID = ?");
                deleteClientServices.setInt(1, serviceID);
                deleteClientServices.executeUpdate();

                deleteInvoiceClient = connect.prepareStatement("delete from vaclientmanager.invoice_client where serviceID = ?");
                deleteInvoiceClient.setInt(1, serviceID);
                deleteInvoiceClient.executeUpdate();

                deleteService = connect.prepareStatement("delete from vaclientmanager.services where serviceID = ?");
                deleteService.setInt(1, serviceID);
                deleteService.executeUpdate();
        
                System.out.println("Service deleted successfully.\n==============================");
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }  
    }
    
    private void DeleteInvoice() throws ParseException
    {
        try 
        {
            ViewInvoiceList();
            System.out.print("Enter the invoice number you want to delete: ");
            String invoiceNum = c.nextLine();
        
            if (InvoiceChecker(invoiceNum)) 
            {
                deleteInvoice = connect.prepareStatement("delete from vaclientmanager.invoice where invoiceNum = ?");
                deleteInvoice.setString(1, invoiceNum);
                deleteInvoice.executeUpdate();
            
                deleteInvoiceClient = connect.prepareStatement("delete from vaclientmanager.invoice_client where invoiceNum = ?");
                deleteInvoiceClient.setString(1, invoiceNum);
                deleteInvoiceClient.executeUpdate();
            
                System.out.println("Invoice deleted successfully.\n==============================");
            } 
            else 
            {
                System.out.println("Invoice number does not exist. Pleas.\n==============================");
                InvoiceList();
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    
    private void DeleteClient() throws ParseException
    {
        try 
        {
            ViewClientList();
            System.out.print("Enter the client ID you want to Delete: ");
            String clientID = c.nextLine();
            if(ListChecker(clientID))
            {
                System.out.println("Are you sure you want to delete this client data?\n1.Yes\n2.No");
                int choice = cl.nextInt();
                if (choice == 1)
                {
                    String invoiceNum = getInvoiceNum(clientID);
                    if(!invoiceNum.isEmpty())
                    {
                        deleteInvoiceClient = connect.prepareStatement("delete from vaclientmanager.invoice_client where invoiceNum = " + invoiceNum);
                        deleteInvoiceClient.executeUpdate();
                    
                        deleteClientInvoice = connect.prepareStatement("delete from vaclientmanager.invoice where clientID = " + clientID);
                        deleteClientInvoice.executeUpdate();
                    }
                    deleteClientServices = connect.prepareStatement("delete from vaclientmanager.client_services where clientID = " + clientID);
                    deleteClientServices.executeUpdate();
                
                    deleteClient = connect.prepareStatement("delete from vaclientmanager.client where clientID = ?");
                    deleteClient.setString(1,clientID);
                    deleteClient.executeUpdate();
                    System.out.println("Client information deleted successfully.\n==============================");
                }
            }
            else
                {
                    System.out.println("Client Number entered does not exist.\n==============================");
                    ExistingClient();
                }
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    
    private void ServiceList()
    {
        try 
        {
            System.out.println("1.View\n2.Add\n3.Update\n4.Delete\n5.Total Hours per Service\n6.Back");
            int choice = cl.nextInt();
            switch (choice) 
            {
                case 1:
                {
                    ViewServiceList();
                    ServiceList();
                    break;
                }
                case 2:
                {
                    AddNewService();
                    break;
                }
                case 3:
                {
                    UpdateService();
                    break;
                }
                case 4:
                {
                    DeleteService();
                    break;
                }
                case 5:
                {
                    TotalHoursperServices();
                    break;
                }
                case 6:
                {
                    break;
                }
                default:
                    System.out.println("Choose from the choices given above.");
                    ServiceList();
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    
    private void ExistingClient() throws ParseException 
    {
        try 
        {
            System.out.println("1.View\n2.Update\n3.Delete\n4.New Service Request\n5.Total Bill per Client\n6.Back");
            int choice = cl.nextInt();
            switch (choice) 
            {
                case 1:
                {
                    ViewClientList();
                    ExistingClient();
                    break;
                }
                case 2:
                {
                    UpdateClientList();
                    break;
                }
                case 3:
                {
                    DeleteClient();
                    break;
                }
                case 4:
                {
                    AddData();
                    break;
                }
                case 5:
                {
                    TotalBillperClient();
                    break;
                }
                case 6:
                {
                    break;
                }
                default:
                    System.out.println("Choose from the choices given above.");
                    ServiceList();
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    
    private void InvoiceList() throws ParseException
    {
        try 
        {
            System.out.println("1.View\n2.Generate\n3.Edit\n4.Delete\n5.Back");
            int choice = cl.nextInt();
            switch (choice) 
            {
                case 1:
                {
                    ViewInvoiceList();
                    InvoiceList();
                    break;
                }
                case 2:
                {
                    GenerateInvoice();
                    break;
                }
                case 3:
                {
                    EditInvoice();
                    break;
                }
                case 4:
                {
                    DeleteInvoice();
                    break;
                }
                case 5:
                {
                    break;
                }
                default:
                    System.out.println("Choose from the choices given above.");
                    ServiceList();
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    
    private void closeResources()
    {
        try 
        {
            if (connect != null) connect.close();
            if (newClient != null) newClient.close();
            if (insertServiceData != null) insertServiceData.close();
            if (ServicesList != null) ServicesList.close();
            if (ClientList != null) ClientList.close();
            if (InvoiceList != null) InvoiceList.close();
            if (InvoiceClientList != null) InvoiceClientList.close();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    } 

    private boolean ListChecker(String clientID) 
    {
        try 
        {
            ClientList = connect.createStatement().executeQuery("select clientID from vaclientmanager.client");
            while (ClientList.next()) 
            {
                String ID = ClientList.getString("clientID");
                if (ID.equals(clientID))
                    return true;
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkServiceExists(int serviceID) throws SQLException 
    {
        checkService = connect.prepareStatement("select count(*) from vaclientmanager.services where serviceID = ?");
        checkService.setInt(1, serviceID);
        ResultSet result = checkService.executeQuery();
        result.next();
        int count = result.getInt(1);
        return count > 0;
    }

    private boolean InvoiceChecker(String invoiceNum) 
    {
        try 
        {
            InvoiceList = connect.createStatement().executeQuery("select invoiceNum from vaclientmanager.invoice");
            while (InvoiceList.next()) 
            {
                String ID = InvoiceList.getString("invoiceNum");
                if (ID.equals(invoiceNum))
                    return true;
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return false;
    }

    private String getClientIDFromInvoice(String invoiceNum) throws SQLException 
    {
        String clientID = null;
        checkInvoice = connect.prepareStatement("select clientID from vaclientmanager.invoice where invoiceNum = " + invoiceNum);
        InvoiceList = checkInvoice.executeQuery();
        if (InvoiceList.next()) 
        {
            clientID = InvoiceList.getString("clientID");
        }
        return clientID;
    }

    private void UpdateServiceHours(String invoiceNum) 
    {
        try 
        {
            ViewInvoiceClientList(invoiceNum);
            System.out.print("Enter the service ID you want to update: ");
            String serviceID = c.nextLine();
        
            System.out.print("Enter the new hours of work: ");
            int newHours = cl.nextInt();
        
            updateInvoiceClient = connect.prepareStatement("update vaclientmanager.invoice_client set workHours = " + newHours + " where serviceID = " + serviceID);
            updateInvoiceClient.executeUpdate();
            
            System.out.println("Hours of work updated successfully.\n==============================");
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    private void Others() throws ParseException 
    {
        try
        {
            System.out.println("1.Check most popular service\n2.Check Total Income\n3.Top Client\n4.Back");
            int choice = cl.nextInt();
            switch (choice)
            {
                case 1:
                {
                    getPopularService();
                    break;
                }
                case 2:
                {
                    getTotalIncome();
                    break;
                }
                case 3:
                {
                    getTopClient();
                    break;
                }
                case 4:
                {
                    break;
                }
                default:
                    System.out.println("Invalid choice.");
                    Others();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private void getPopularService() throws SQLException, ParseException 
    {
        try
        {
            System.out.print("Start Date (yyyy-mm-dd): ");
            String startDateInput = c.nextLine();
            java.util.Date startDate = dft.parse(startDateInput);

            System.out.print("End Date (yyyy-mm-dd): ");
            String endDateInput = c.nextLine();
            java.util.Date endDate = dft.parse(endDateInput);

            String startDateStr = dft.format(startDate);
            String endDateStr = dft.format(endDate);
            
            int counter = ViewServices() - 100;
            int[][] services = new int[counter][2];

            ServicesList = statement.executeQuery("select serviceID, count(serviceID) as popularity from vaclientmanager.client_services where requestDate between '" + startDateStr + "' and '" + endDateStr + "' group by serviceID order by popularity desc");
        
            int index = 0;
            while (ServicesList.next()) 
            {
                String serviceID = ServicesList.getString("serviceID");
                int popularity = ServicesList.getInt("popularity");
                services[index][0] = Integer.parseInt(serviceID);
                services[index][1] = popularity;
                index++;
            }

            System.out.println("==============================\nPopular Services:");
            for (int i = 0; i < counter-1; i++) 
            {
                String description = getServiceDescription(String.valueOf(services[i][0]));
                int popularity = services[i][1];
                System.out.println("Service ID: " + services[i][0] + ", Description: " + description + ", Popularity: " + popularity);
            }
            System.out.println("==============================");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (ParseException i)
        {
            System.out.println("Please follow the format (yyyy-mm-dd).");
            getPopularService();
        }
    }

    private String getServiceDescription(String serviceID) throws SQLException 
    {
        String description = "";

        ServicesList = connect.createStatement().executeQuery("select sDescription from vaclientmanager.services where serviceID = " + serviceID);

        if (ServicesList.next()) 
        {
            description = ServicesList.getString("sDescription");
        }
    return description;
    }

    private void getTotalIncome() throws ParseException, SQLException 
    {
       try 
       {
            System.out.print("Start Date (yyyy-mm-dd): ");
            String startDateInput = c.nextLine();
            java.util.Date startDate = dft.parse(startDateInput);

            System.out.print("End Date (yyyy-mm-dd): ");
            String endDateInput = c.nextLine();
            java.util.Date endDate = dft.parse(endDateInput);

            String startDateStr = dft.format(startDate);
            String endDateStr = dft.format(endDate);

            String query = "SELECT SUM(workHours * rate) AS totalIncome FROM vaclientmanager.invoice_client ic " +
                "JOIN vaclientmanager.services s ON ic.serviceID = s.serviceID " +
                "JOIN vaclientmanager.invoice i ON ic.invoiceNum = i.invoiceNum " +
                "WHERE i.currentDate BETWEEN ? AND ?";


            PreparedStatement preparedStatement = connect.prepareStatement(query);
            preparedStatement.setString(1, startDateStr);
            preparedStatement.setString(2, endDateStr);


            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("==============================");
            if (resultSet.next()) 
            {
                int totalIncome = resultSet.getInt("totalIncome");
                System.out.println("Total Income within the specified date range: $" + totalIncome);
            } 
            else 
            {
                System.out.println("No income within the specified date range.");
            }
            System.out.println("==============================");
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        catch(ParseException i)
        {
            System.out.println("Please follow the format (yyyy-mm-dd).");
            getTotalIncome();
        }
    }
    
    private void getTopClient() throws SQLException, ParseException
    {
        try 
        {
            System.out.print("Enter Start Date (yyyy-mm-dd): ");
            String startDateInput = c.nextLine();
            java.util.Date startDate = dft.parse(startDateInput);

            System.out.print("Enter End Date (yyyy-mm-dd): ");
            String endDateInput = c.nextLine();
            java.util.Date endDate = dft.parse(endDateInput);

            String startDateStr = dft.format(startDate);
            String endDateStr = dft.format(endDate);
        
            PreparedStatement statement = connect.prepareStatement("select c.clientID, c.FName, c.LName, sum(ic.workHours * s.rate) as totalAmount " +
                "from client c inner join invoice i ON c.clientID = i.clientID " +
                "inner join invoice_client ic ON i.invoiceNum = ic.invoiceNum " +
                "inner join services s ON ic.serviceID = s.serviceID " +
                "inner join client_services cs ON i.clientID = cs.clientID AND ic.serviceID = cs.serviceID " +
                "where requestDate between ? and ? group by c.clientID order by totalAmount desc limit 1");

            statement.setString(1, startDateStr);
            statement.setString(2, endDateStr);

            ResultSet resultSet = statement.executeQuery();
            System.out.println("==============================");
            if (resultSet.next()) 
            {
                int clientID = resultSet.getInt("clientID");
                String firstName = resultSet.getString("FName");
                String lastName = resultSet.getString("LName");
                double totalAmount = resultSet.getDouble("totalAmount");

                System.out.println("Top Client:");
                System.out.println("Client ID: " + clientID);
                System.out.println("Name: " + firstName + " " + lastName);
                System.out.println("Total Amount Spent: $" + totalAmount);
            }
            else 
            {
                System.out.println("No clients found within the specified date range.");
            }
            System.out.println("==============================");
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        catch (ParseException e) 
        {
            System.out.println("Please follow the format (yyyy-mm-dd).");
            getTopClient();
        }
    }
}

public class ClientManager 
{
    public static void main(String[] args) throws Exception 
    {
        SQLAccess SQL = new SQLAccess();
        SQL.readDataBase();
    }   
}