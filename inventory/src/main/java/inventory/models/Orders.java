package inventory.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;

/**
 * Entity: inventorydb.orders
 *
 */
@Entity
@Table(name = "orders")
public class Orders {
  
  // Use generated ID
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "orderId")
  private long orderId;
  
  // Item id
  @NotNull
  @Column(name = "itemId")
  private long itemId;

  // Customer id
  @NotNull
  @Column(name = "customerId")
  private String customerId;

  // Count
  @NotNull
  @Column(name = "count")
  private int count;

  public Orders() { }

  public Orders(long id) { 
    this.orderId = id;
  }
  
  public Orders(long itemId, String customerId, int count) {
    this.itemId = itemId;
    this.customerId = customerId;
    this.count = count;
  }

  public long getOrderId() {
    return orderId;
  }

  public void setOrderId(long value) {
    this.orderId = value;
  }

  public long getItemId() {
    return itemId;
  }

  public void setItemId(long value) {
    this.itemId = value;
  }

  public String getCustomerId() {
    return customerId;
  }
  
  public void setCustomerId(String value) {
    this.customerId = value;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int value) {
    this.count = value;
  }

  public String toString() {
    StringBuilder string = new StringBuilder();
    string.append("{\n");
    string.append(String.format("\t\"orderId\": %s,\n", this.orderId));
    string.append(String.format("\t\"itemId\": \"%s\",\n", this.itemId));
    string.append(String.format("\t\"customerId\": \"%s\",\n", this.customerId));
    string.append(String.format("\t\"count\": %s\n", this.count));
    string.append("}");

    return string.toString();
  }
}
