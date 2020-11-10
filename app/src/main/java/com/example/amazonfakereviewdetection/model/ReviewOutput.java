package com.example.amazonfakereviewdetection.model;

public class ReviewOutput {

    private double percentFakeReview;

    private double averageConfidence;

    public ReviewOutput(double percentFakeReview, double averageConfidence) {
        this.percentFakeReview = percentFakeReview;
        this.averageConfidence = averageConfidence;
    }

    public double getPercentFakeReview() {
        return percentFakeReview;
    }

    public void setPercentFakeReview(double percentFakeReview) {
        this.percentFakeReview = percentFakeReview;
    }

    public double getAverageConfidence() {
        return averageConfidence;
    }

    public void setAverageConfidence(double averageConfidence) {
        this.averageConfidence = averageConfidence;
    }
}