package models;

public class EventParams {
    private String orderNumberAndType;
    private boolean invertResult;
    private boolean hasExceptionBlock;

    public EventParams(String orderNumberAndType, boolean invertResult, boolean hasExceptionBlock) {
        this.orderNumberAndType = orderNumberAndType;
        this.invertResult = invertResult;
        this.hasExceptionBlock = hasExceptionBlock;
    }

    public String getOrderNumberAndType() {
        return orderNumberAndType;
    }

    public boolean isInvertResult() {
        return invertResult;
    }

    public boolean isHasExceptionBlock() {
        return hasExceptionBlock;
    }

    public void setHasExceptionBlock(boolean hasExceptionBlock) {
        this.hasExceptionBlock = hasExceptionBlock;
    }
}
