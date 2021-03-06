package fiap.com.br.alurafoodapplication.ui;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fiap.com.br.alurafoodapplication.R;
import fiap.com.br.alurafoodapplication.dao.ClientDao;
import fiap.com.br.alurafoodapplication.mask.CpfMask;
import fiap.com.br.alurafoodapplication.mask.PhoneMask;
import fiap.com.br.alurafoodapplication.model.ClientModel;
import fiap.com.br.alurafoodapplication.validator.CpfValidator;
import fiap.com.br.alurafoodapplication.validator.EmailValidator;
import fiap.com.br.alurafoodapplication.validator.PasswordValidator;
import fiap.com.br.alurafoodapplication.validator.PhoneValidator;
import fiap.com.br.alurafoodapplication.validator.StandardValidator;
import fiap.com.br.alurafoodapplication.validator.Validator;

public class FormRegisterActivity extends AppCompatActivity {

    private final List<Validator> validators = new ArrayList<>();
    private EditText name;
    private EditText fieldCpf;
    private EditText fieldPhone;
    private EditText fieldEmail;
    private EditText fieldPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeFields();
    }

    private void initializeFields() {
        setUpFieldName();
        setUpFieldCpf();
        setUpFieldPhone();
        setUpFieldEmail();
        setUpFieldPassword();
        setUpButtonRegister();
    }

    private void setUpFieldName() {
        TextInputLayout tilName = findViewById(R.id.form_til_name);
        name = tilName.getEditText();
        validateEmptyField(tilName);
    }

    private void setUpFieldCpf() {
        TextInputLayout tilCpf = findViewById(R.id.form_til_cpf);
        final CpfValidator validator = new CpfValidator(tilCpf, getApplicationContext());
        fieldCpf = tilCpf.getEditText();
        validators.add(validator);
        fieldCpf.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                final String cpf = fieldCpf.getText().toString();
                if (hasFocus) {
                    fieldCpf.setText(CpfMask.unmask(cpf));
                } else {
                    validator.isValid();
                }
            }
        });
    }

    private void setUpFieldPhone() {
        TextInputLayout tilPhone = findViewById(R.id.form_til_phone);
        fieldPhone = tilPhone.getEditText();
        final PhoneValidator validator = new PhoneValidator(tilPhone, getApplicationContext());
        final PhoneMask phoneMask = new PhoneMask();
        validators.add(validator);
        fieldPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String phoneWithMask = fieldPhone.getText().toString();
                if (hasFocus) {
                    String phoneWithoutMask = phoneMask.unMaskPhone(phoneWithMask);
                    fieldPhone.setText(phoneWithoutMask);
                } else {
                    validator.isValid();
                }
            }
        });

    }

    private void setUpFieldEmail() {
        TextInputLayout tilEmail = findViewById(R.id.form_til_email);
        fieldEmail = tilEmail.getEditText();
        final EmailValidator validator = new EmailValidator(tilEmail, getApplicationContext());
        validators.add(validator);
        fieldEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validator.isValid();
                }
            }
        });

    }

    private void setUpFieldPassword() {
        TextInputLayout tilPassword = findViewById(R.id.form_til_password);
        fieldPassword = tilPassword.getEditText();
        final PasswordValidator validator = new PasswordValidator(tilPassword, getApplicationContext());
        validators.add(validator);
        fieldPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validator.isValid();
                }
            }
        });
    }

    private void setUpButtonRegister() {
        Button btnRegister = findViewById(R.id.form_btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean formIsValid = validAllFields();
                if (formIsValid) {
                    ClientModel client = new ClientModel();
                    ClientDao clientDao = new ClientDao(FormRegisterActivity.this);
                    client.setName(name.getText().toString());
                    client.setCpf(fieldCpf.getText().toString());
                    client.setPhone(fieldPhone.getText().toString());
                    client.setEmail(fieldEmail.getText().toString());
                    client.setPassword(fieldPassword.getText().toString());

                    clientDao.insertClient(client);


                    Toast.makeText(
                            FormRegisterActivity.this,
                            "Cliente inserido com sucesso",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void validateEmptyField(final TextInputLayout tilField) {
        final EditText field = tilField.getEditText();
        final StandardValidator validator = new StandardValidator(tilField, getApplicationContext());
        validators.add(validator);
        field.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validator.isValid();

                }
            }
        });
    }

    private boolean validAllFields() {
        boolean formIsValid = true;
        for (Validator validator : validators) {
            if (!validator.isValid()) {
                formIsValid = false;
            }
        }
        return formIsValid;
    }

}
