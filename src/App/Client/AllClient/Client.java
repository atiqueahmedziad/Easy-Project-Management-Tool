package App.Client.AllClient;

import javafx.beans.property.SimpleStringProperty;

public class Client {
    private SimpleStringProperty clientId;
    private SimpleStringProperty clientName;
    private SimpleStringProperty contactPerson;
    private SimpleStringProperty phone;
    private SimpleStringProperty address;

    public Client(String clientId, String clientName, String contactPerson, String phone, String address) {
        this.clientId = new SimpleStringProperty(clientId);
        this.clientName = new SimpleStringProperty(clientName);
        this.contactPerson = new SimpleStringProperty(contactPerson);
        this.phone = new SimpleStringProperty(phone);
        this.address = new SimpleStringProperty(address);
    }

    public String getClientId() {
        return clientId.get();
    }

    public void setClientId(String id) {
        this.clientId = new SimpleStringProperty(id);
    }

    public String getClientName() {
        return clientName.get();
    }

    public void setClientName(String clientName) {
        this.clientName = new SimpleStringProperty(clientName);
    }

    public String getContactPerson() {
        return contactPerson.get();
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = new SimpleStringProperty(contactPerson);
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone = new SimpleStringProperty(phone);
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address = new SimpleStringProperty(address);
    }
}
