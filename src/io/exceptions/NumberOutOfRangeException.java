package io.exceptions;

/**
 *
 * @author Jacek
 */
public class NumberOutOfRangeException extends Exception {

	private static final long serialVersionUID = 5193544788629965620L;
	
	private Integer suggestedValue;
	
    /**
     *
     */
    public NumberOutOfRangeException() {
		suggestedValue = null;
	}
	
    /**
     *
     * @param suggestedValue
     */
    public NumberOutOfRangeException(int suggestedValue) {
		this.suggestedValue = suggestedValue; 
	}
	
    /**
     *
     * @return
     */
    public boolean hasSuggestedValue() {
		return suggestedValue != null;
	}
	
    /**
     *
     * @return
     */
    public Integer getSuggestedValue() {
		return suggestedValue;
	}
	
}
