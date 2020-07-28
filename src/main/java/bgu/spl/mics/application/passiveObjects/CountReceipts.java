package bgu.spl.mics.application.passiveObjects;

public class CountReceipts {

    private static class SingeltonHolder {
        private static CountReceipts instance = new CountReceipts();
    }
    private int nextReceiptNum;

    public static CountReceipts getInstance() {
        return SingeltonHolder.instance;
    }

    public synchronized int getNextReceiptsNumber() {
        int output = nextReceiptNum;
        nextReceiptNum++;
        return output;
    }

}
