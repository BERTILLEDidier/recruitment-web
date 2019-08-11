package fr.d2factory.libraryapp.member;

public class Student extends Member {

    private boolean stud1;

    public Student(boolean annee) {
        super();
        this.stud1 = annee;
    }

    @Override
    public void payBook(int numberOfDays) {
        float price = 0;
        if (stud1 == true) {
            if (numberOfDays > 15) {
                price = (float) ((numberOfDays - 15) * 0.10);
            }
        } else {
            price = (float) (numberOfDays*0.10);
        }

        if(numberOfDays >30){
            long aux = numberOfDays - 30;
            price += aux*0.15;
        }
        this.setWallet(this.getWallet() - price);
    }

    @Override
    public boolean isTooLate(int numberOfDays) {
        if(numberOfDays>30){
            return true;
        }
        return false;

    }

    public boolean isStud1() {
        return stud1;
    }

    public void setStud1(boolean stud1) {
        this.stud1 = stud1;
    }
}
