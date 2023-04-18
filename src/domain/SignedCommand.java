package src.domain;

import java.io.Serializable;
import java.security.SignedObject;
import java.security.cert.Certificate;

public class SignedCommand implements Serializable {

    public SignedObject signedObject;
    public transient Certificate certificate;

    public SignedCommand(SignedObject signedObject, Certificate certificate) {
        this.signedObject = signedObject;
        this.certificate = certificate;
    }
}
