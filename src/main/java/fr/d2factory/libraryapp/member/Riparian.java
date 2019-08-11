package fr.d2factory.libraryapp.member;

public class Riparian extends Member {

    public Riparian() {
        super();
    }

    @Override
    public void payBook(int numberOfDays) {
        float price = (float) (numberOfDays*0.10);
        if(numberOfDays > 60){
            long aux = numberOfDays - 60;
            price += aux*0.20;
        }
        this.setWallet(this.getWallet()-price);
    }

    @Override
    public boolean isTooLate(int numberOfDays) {
        if(numberOfDays>60){
            return true;
        }
        return false;
    }
}
