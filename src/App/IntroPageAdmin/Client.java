package App.IntroPageAdmin;

import javafx.beans.property.SimpleStringProperty;

public class Client {
    private SimpleStringProperty id;
    private SimpleStringProperty clientName;
    private SimpleStringProperty contactPerson;

    public Client(String id, String clientName, String contactPerson) {
        this.id = new SimpleStringProperty(id);
        this.clientName = new SimpleStringProperty(clientName);
        this.contactPerson = new SimpleStringProperty(contactPerson);
    }

    public String getClientId() {
        return id.get();
    }

    public void setClientId(String id) {
        this.id = new SimpleStringProperty(id);
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
}
