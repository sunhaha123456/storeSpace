package com.store.common.data.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@lombok.Data
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BaseDataIdLongDelFlag implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(length = 19)
	protected Long id;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date", nullable = false, updatable = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	protected Date createDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_modified", updatable = true, columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	//@JsonProperty(value = "lastModifiedDate")
	protected Date lastModified;

	// 0：正常，1：已删除
	// 默认 0
	@Column(name = "del_flag", columnDefinition = "TINYINT NOT NULL DEFAULT 0")
	protected Integer delFlag;
}