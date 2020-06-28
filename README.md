# Connector to [Monday.com](https://monday.com)
Application uses [REST JSON API v1](https://monday.com/developers) (will work until October 1, 2020)

This version gets only Boards and Items from Monday.com.

Calls:
- `.../boards` - to receive list of Boards with list of items in each in JSON format.

##Required:
* Created account at Monday.com (trial or paid)
* At least one Board with Items
* Generate personal token for API v1
* Set personal token to **MONDAY_API_TOKEN** environment variable.

###Note:
This application call https endpoints of Monday.com. If you use OpenJDK, or some Linux versions with security bugs then you can receive
 errors like this:

- TrustAnchors parameter must be non-empty
- Unable to find valid certification path to requested target

This means that your trusted storage is empty or doesn't have required root or intermediate CA certificates. Java's Default Truststore is
 `%JAVA_HOME%/jre/lib/security/cacerts` file. Password by default is **"changeit"**. Depending on the different versions of JDK, the
  CACERT keystore may be in different locations.
 
 You can change it to other non-empty trusted storage by setting property `javax.net.ssl.keyStore` with path to keystore
 
 OR
 
 You can add required certificates using **Keytool** similar to:
 ```shell script
keytool -import -trustcacerts -keystore $JAVA_HOME/jre/lib/security/cacerts -storepass changeit -alias Root -import -file Trustedcaroot.txt
```

For now Monday.com uses certificate which is issued by **"Go Daddy"** (valid until November 14, 2021). So, you can download required CA
 certificates from **Go Daddy's** [Repository](https://ssl-ccp.godaddy.com/repository?origin=CALLISTO).