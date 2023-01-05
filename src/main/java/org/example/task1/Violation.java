package org.example.task1;

public class Violation {
    private float maxValueFee;
    private float sumOfFees;

    public Violation() {
        this.maxValueFee = 0;
        this.sumOfFees = 0;
    }

    public Violation(Fee fee) {
        this.maxValueFee = fee.getFineAmount();
        this.sumOfFees = fee.getFineAmount();
    }

    public Violation setNewAmounts(Fee fee){
        if(maxValueFee < fee.getFineAmount()){
            maxValueFee = fee.getFineAmount();
        }
        sumOfFees+=fee.getFineAmount();
        return this;
    }
    public Violation setNewAmounts(float fine, float sum){
        if(maxValueFee < fine){
            maxValueFee = fine;
        }
        sumOfFees+=sum;
        return this;
    }

    public float getMaxValueFee() {
        return maxValueFee;
    }

    public void setMaxValueFee(float maxValueFee) {
        this.maxValueFee = maxValueFee;
    }

    public float getSumOfFees() {
        return sumOfFees;
    }

    public void setSumOfFees(float sumOfFees) {
        this.sumOfFees = sumOfFees;
    }
}
