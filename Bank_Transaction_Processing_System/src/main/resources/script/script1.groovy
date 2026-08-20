import com.sap.gateway.ip.core.customdev.util.Message
import groovy.util.XmlSlurper

def Message processData(Message message) {

    def body = message.getBody(String)
    def xml = new XmlSlurper().parseText(body)

    def amount = xml.Amount.text().toBigDecimal()
    def balance = xml.AvailableBalance.text().toBigDecimal()
    def transactionType = xml.TransactionType.text()

    if (amount <= 0) {

        message.setProperty("ValidationStatus", "FAILED")
        message.setProperty("ValidationMessage", "Invalid Amount")

    } 
    else if (balance < amount) {

        message.setProperty("ValidationStatus", "FAILED")
        message.setProperty("ValidationMessage", "Insufficient Balance")

    } 
    else if (!(transactionType in ["NEFT","RTGS","IMPS","UPI"])) {

        message.setProperty("ValidationStatus", "FAILED")
        message.setProperty("ValidationMessage", "Invalid Transaction Type")

    } 
    else {

        message.setProperty("ValidationStatus", "SUCCESS")
        message.setProperty("ValidationMessage", "Validation Successful")

    }

    return message
}