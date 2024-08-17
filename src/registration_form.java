import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class registration_form extends JDialog {
    private JTextField tfname;
    private JTextField tfemail;
    private JTextField tfpassword;
    private JPanel mainpanel;
    private JTextField registrationFormTextField;
    private JTextField tfphone;
    private JTextField tfconfpass;
    private JButton submitButton;
    private JButton cancelButton;

    public registration_form(JFrame parent) {
        //  this.tfname = tfname;
        super(parent);
        setVisible(true);
        setTitle("User Registration");
        setSize(300, 300);
        setContentPane(mainpanel);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    private void registerUser()
    {
        String name= tfname.getText();
        String email= tfemail.getText();
        String phone= tfphone.getText();
        String password=String.valueOf(tfpassword.getText());
        String confirmpassword=String.valueOf(tfconfpass.getText());

        if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty())
        {
            JOptionPane.showMessageDialog(this,"all fields mandatory","try again", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(!password.equals((confirmpassword)))
        {
            JOptionPane.showMessageDialog(this,"password and confirm password mismatch","try again",JOptionPane.ERROR_MESSAGE);
            return;
 
        }
        user=addUserToDatabase(name,email,phone,password);
        if(user!=null)
        {
            dispose();
        }
        else {
            JOptionPane.showMessageDialog(this,"user failed to register","try again",JOptionPane.ERROR_MESSAGE);
        }
    }
    public User user;
    public User addUserToDatabase(String name, String email, String phone, String password)
    {
        User user=new User();
        String URL="jdbc:postgresql://localhost:5432/registration";
        String USERNAME="postgres";
        String PASSWORD="cmrit";
        try {
            Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement st = con.createStatement();
            String sql = "insert into registration(name,email,phone,password)" + "values(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, password);
            ps.executeUpdate();
            int addedrows = ps.executeUpdate();
            if (addedrows > 0) {
                user.name = name;
                user.email = email;
                user.phone = phone;
                user.password = password;
                //  user.confpass= tfconfirm;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return user;
    }
    public static void main(String args[])
    {
        registration_form r = new registration_form(null);
        User user=r.user;
        if(user!=null)
        {
            System.out.println("Registration successful");
        }
        else {
            System.out.println("Registration not successful");
        }
    }
}
