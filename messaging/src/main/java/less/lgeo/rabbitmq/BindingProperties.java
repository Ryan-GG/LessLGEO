package less.lgeo.rabbitmq;

public abstract class BindingProperties {

  private String exchange = null;
  private String queue = null;
  private String routingKey = null;

  public String getExchange() {
    if (exchange == null) {
      throw new IllegalStateException("Exchange is null");
    }
    return exchange;
  }

  public void setExchange(String exchange) {
    this.exchange = exchange;
  }

  public String getQueue() {
    if (queue == null) {
      throw new IllegalStateException("Queue is null");
    }
    return queue;
  }

  public void setQueue(String queue) {
    this.queue = queue;
  }

  public String getRoutingKey() {
    if (routingKey == null) {
      throw new IllegalStateException("RoutingKey is null");
    }
    return routingKey;
  }

  public void setRoutingKey(String routingKey) {
    this.routingKey = routingKey;
  }
}
