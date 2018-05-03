BEGIN TRANSACTION;

ALTER TABLE `my_records` ADD `aptId` TEXT;

ALTER TABLE "addNewPatient" RENAME TO "addNewPatient_OLD";

CREATE TABLE "addNewPatient" (
	`dataId`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`salutation`	INTEGER,
	`firstName`	TEXT,
	`middleName`	TEXT,
	`lastName`	TEXT,
	`mobileNo`	TEXT,
	`age`	INTEGER,
	`gender`	TEXT,
	`referenceID`	TEXT,
	`clinicID`	TEXT,
	`cityId`	INTEGER,
	`city`	TEXT,
	`dob`	TEXT,
	`outstandingAmount`	NUMERIC,
	`imageURL`	TEXT,
	`email`	TEXT,
	`createdTimeStamp`	TEXT,
	`isInsertedOffline`	INTEGER,
	`isSync`	INTEGER,
	`hospitalPatId`	TEXT,
	`patientId`	TEXT
);

INSERT INTO addNewPatient (`dataId`,`salutation`,`firstName`,`middleName`,`lastName`,`mobileNo`,`age`,`gender`,
`referenceID`,`clinicID`,`cityId`,`city`,`dob`,`outstandingAmount`,`imageURL`,`email`,`createdTimeStamp`,`isInsertedOffline`,
`isSync`,`hospitalPatId`,`patientId`)
  SELECT `dataId`,"",`firstName`,`middleName`,`lastName`,`mobileNo`,`age`,`gender`,`referenceID`,`clinicID`,`cityID`,"",
"","0.00","","",`createdTimeStamp`,"1","0",`hospitalPatientID`,`patientOfflineID`
  FROM addNewPatient_OLD;

DROP TABLE addNewPatient_OLD;



COMMIT;
