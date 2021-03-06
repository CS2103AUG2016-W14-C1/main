package seedu.manager.model.activity;

/**
 *  Class for all types of activity in Remindaroo.
 */

public class Activity implements ReadOnlyActivity, Comparable<Activity> {
    
    public static final String MESSAGE_DATE_CONSTRAINTS = "Event has already ended before it starts.";
    
    private ActivityType type;
	private String name;
	private Status status;
	private AMDate dateTime;
	private AMDate endDateTime;
	private boolean selected;
	
	//@@author A0135730M
	
	// Constructor for required fields
	public Activity(String name) {
	    this.type = ActivityType.FLOATING;
	    this.name = name;
		this.status = new Status();
	}
    
    //@@author A0139797E
    // Wrapper constructor for ReadOnlyActivity
    public Activity(ReadOnlyActivity source) {
        this.type = source.getType();
        this.name = source.getName();
        this.status = new Status(source.getStatus());
        if (source.getDateTime() != null) {
            this.dateTime = new AMDate(source.getDateTime().getTime());
        } else {
            this.dateTime = null;
        }
        if (source.getEndDateTime() != null) {
            this.endDateTime = new AMDate(source.getEndDateTime().getTime());
        } else {
            this.endDateTime = null;
        }
        this.selected = source.getSelected();
    }
	
    //@@author A0135730M
    @Override
    public ActivityType getType() {
        return type;
    }
    
    public void setType(ActivityType type) {
        this.type = type;
    }
    
	@Override
	//@@author A0139797E
    public String getName() {
	    return name;
	}
	
    public void setName(String newName) {
		this.name = newName;
	}
	
    public boolean getSelected() {
        return selected;
    }
    
    public void setSelected(boolean isSelected) {
        this.selected = isSelected;
    }
    
	//@@author A0144704L
	public void setStatus(boolean completed) {
		(this.status).setStatus(completed);
	}
	
	@Override
	public Status getStatus() {
		return this.status;
	}
	
	public boolean isExpired(AMDate date) {
		AMDate today = new AMDate("today");
		return today.getTime() > date.getTime();
	}
	
	//@@author A0139797E
	@Override
    public AMDate getDateTime() {
        return dateTime;
    }
    
	@Override
    public AMDate getEndDateTime() {
        return endDateTime;
    }
    
	//@@author A0135730M
    public void setDateTime(String newDateTime) {
        assert newDateTime != null;
        assert !this.type.equals(ActivityType.FLOATING);
        if (this.dateTime == null) {
            this.dateTime = new AMDate(newDateTime);
        } else {
            this.dateTime.setAMDate(newDateTime);
        }
    }
    
    //@@author A0139797E
    public void setDateTime(long epochDateTime) {
        this.dateTime = new AMDate(epochDateTime);
    }
    
    public void setEndDateTime(String newEndDateTime) {
        assert !this.type.equals(ActivityType.FLOATING);
        // remove endDateTime if activity is converted to deadline
        if (this.type.equals(ActivityType.DEADLINE)) {
            assert newEndDateTime == null;
            this.endDateTime = null;
        } else if (this.type.equals(ActivityType.EVENT)) {
            if (this.endDateTime == null) {
                this.endDateTime = new AMDate(newEndDateTime);
            } else {
                this.endDateTime.setAMDate(newEndDateTime);
            }
        }
    }
	
    public void removeEndDateTime() {
        this.endDateTime = null;
    }
    
    public void setEndDateTime(long epochEndDateTime) {
        this.endDateTime = new AMDate(epochEndDateTime);
    }
    
    //@@author A0135730M
    public void setOffset(int offset, String unit) {
        assert !this.type.equals(ActivityType.FLOATING);
        this.dateTime.addOffset(offset, unit);
        if (this.endDateTime != null) {
            this.endDateTime.addOffset(offset, unit);
        }
    }
    
    @Override
    public String toString() {
        return name;
    }
    
	@Override
	public boolean equals(Object o) {
	    return o == this
	                // basic Activity equality
	            || (o instanceof Activity
	                && this.name.equals(((Activity)o).name)
	                && this.status.equals(((Activity)o).status)
	                && this.type.equals(((Activity)o).type)
	                   // floating equality
	                && (this.type.equals(ActivityType.FLOATING)
	                   // deadline equality
	                   || (this.type.equals(ActivityType.DEADLINE)
	                      && this.dateTime.equals(((Activity)o).dateTime))
	                   // event equality
	                   || (this.type.equals(ActivityType.EVENT)
	                      && this.dateTime.equals(((Activity)o).dateTime)
	                      && this.endDateTime.equals(((Activity)o).endDateTime)))
	                );
	}
	
    @Override
    //@@author A0139797E
    public int compareTo(Activity other) {
        // Check for floating tasks
        if (this.type.equals(ActivityType.FLOATING) && other.type.equals(ActivityType.FLOATING)) {
            if (!this.getStatus().isCompleted() && other.getStatus().isCompleted()) {
                return -1;
            } else if (this.getStatus().isCompleted() && !other.getStatus().isCompleted()) {
                return 1;
            } else { 
                return 0; 
            }
        } else if (other.type.equals(ActivityType.FLOATING)) {
            return -1;
        } else if (this.type.equals(ActivityType.FLOATING)) {
            return 1;
        } else if (!this.getStatus().isCompleted() && other.getStatus().isCompleted()) {
        	return -1; 
        } else if (this.getStatus().isCompleted() && !other.getStatus().isCompleted()) { 
        	return 1; 
        } else {
	        // Comparison between 2 deadlines
	        if (this.type.equals(ActivityType.DEADLINE) && other.type.equals(ActivityType.DEADLINE)) {
	           return this.getDateTime().getTime().compareTo(other.getDateTime().getTime());	
	        // Comparisons between a deadline and an event
	        } else if (this.type.equals(ActivityType.EVENT) && other.type.equals(ActivityType.DEADLINE) ||
	                this.type.equals(ActivityType.DEADLINE) && other.type.equals(ActivityType.EVENT)) {
	            return this.getDateTime().getTime().compareTo(other.getDateTime().getTime());
	        // Comparisons between 2 events
	        } else {
	           int startTimeCompare = this.getDateTime().getTime().compareTo(other.getDateTime().getTime());     
	           if (startTimeCompare == 0) {
	               return this.getEndDateTime().getTime().compareTo(other.getEndDateTime().getTime());
	           } else {
	               return startTimeCompare;
	           }
	        }
    	}
    }
}
