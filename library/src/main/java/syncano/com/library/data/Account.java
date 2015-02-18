package syncano.com.library.data;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Represents Account object from Syncano Api
 */
public class Account implements Serializable {
    private static final long serialVersionUID = -6806030269092528126L;

    /** account id */
    @Expose
    private String id;
    /** account email */
    @Expose
    private String email;
    /** account first name */
    @Expose
    @SerializedName(value = "first_name")
    private String firstName;
    /** account last name */
    @Expose
    @SerializedName(value = "last_name")
    private String lastName;

    /** account id */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /** account email */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /** account first name */
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /** account last name */
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
