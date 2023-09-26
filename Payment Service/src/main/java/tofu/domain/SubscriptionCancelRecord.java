package tofu.domain;

/**"record" is a new feature introduced in Java 14 that provides a concise way to define classes
 *  that are mainly used to hold data. It is a special type of class designed
 *  specifically for immutable data models.
 *  The record class is declared using the record keyword.
 * */
public record SubscriptionCancelRecord(String status) {
}
