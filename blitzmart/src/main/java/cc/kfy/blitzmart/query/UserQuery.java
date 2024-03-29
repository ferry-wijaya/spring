package cc.kfy.blitzmart.query;

public class UserQuery {
    public static final String INSERT_USER_QUERY = "INSERT INTO Users(first_name, last_name, email, password) " + "VALUES (:firstName, :lastName, :email, :password)";
    public static final String COUNT_USER_EMAIL_QUERY = "SELECT COUNT(*) FROM Users WHERE email = :email";
    public static final String INSERT_VERIFICATION_QUERY = "INSERT INTO AccountVerifications(user_id, url) VALUES(:userId, :url)";
    public static final String SELECT_USER_BY_EMAIL_QUERY = "SELECT * FROM Users WHERE email = :email";
    public static final String DELETE_VERIFICATION_CODE_BY_USER_ID_QUERY = "DELETE FROM TwoFactorVerifications WHERE user_id = :user_id";
    public static final String INSERT_VERIFICATION_CODE_QUERY = "INSERT INTO TwoFactorVerifications(user_id, code, expiration_date) VALUES(:user_id, :code, :expiration_date)";
    public static final String SELECT_USER_BY_USER_CODE_QUERY = "SELECT * FROM Users WHERE id = (SELECT user_id FROM TwoFactorVerifications WHERE code = :code)";
    public static final String  DELETE_CODE_QUERY = "DELETE FROM TwoFactorVerifications WHERE code = :code";
    public static final String SELECT_CODE_EXPIRATION_QUERY = "SELECT (expiration_date < NOW()) AS is_expired FROM TwoFactorVerifications WHERE code = :code";
}
