package com.telappoint.resvdeskrestws.common.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class Seat {
	// online
	private String seatId;
	private String seatNumber;
	private Long eventDateTimeId;
	private String displaySeatNumber;
	private Long scheduleId;
	private Integer placement;
	private Integer rowId;
	private Integer columnId;

	// ivr
	private String seatAudio;
	private String seatTTS;

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	public String getSeatAudio() {
		return seatAudio;
	}

	public void setSeatAudio(String seatAudio) {
		this.seatAudio = seatAudio;
	}

	public String getSeatTTS() {
		return seatTTS;
	}

	public void setSeatTTS(String seatTTS) {
		this.seatTTS = seatTTS;
	}

	public String getSeatId() {
		return seatId;
	}

	public void setSeatId(String seatId) {
		this.seatId = seatId;
	}

	public Long getEventDateTimeId() {
		return eventDateTimeId;
	}

	public void setEventDateTimeId(Long eventDateTimeId) {
		this.eventDateTimeId = eventDateTimeId;
	}

	public String getDisplaySeatNumber() {
		return displaySeatNumber;
	}

	public void setDisplaySeatNumber(String displaySeatNumber) {
		this.displaySeatNumber = displaySeatNumber;
	}

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Integer getRowId() {
		return rowId;
	}

	public void setRowId(Integer rowId) {
		this.rowId = rowId;
	}

	public Integer getColumnId() {
		return columnId;
	}

	public void setColumnId(Integer columnId) {
		this.columnId = columnId;
	}

	public Integer getPlacement() {
		return placement;
	}

	public void setPlacement(Integer placement) {
		this.placement = placement;
	}


}
