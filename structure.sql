CREATE DATABASE IF NOT EXISTS employee;

use employee;

CREATE TABLE IF NOT EXISTS EMP_MASTER(id serial, empId varchar(50) primary key, empName varchar(100) not null , email varchar(100) not null , age int(3) not null , location varchar(10), regime varchar(10));

CREATE TABLE IF NOT EXISTS INCOME_MASTER(incomeId serial primary key, empId varchar(50) not null,basicSalary double(9,2), pf double(9,2), gratuity double(9,2), medical double(9,2), hra double(9,2), lta double(9,2), specialAllowances double(9,2), incomeOtherSources double(9,2), foreign key(empId) references emp_master(empId));

CREATE TABLE IF NOT EXISTS DEDUCTION_MASTER(deductionId serial primary key, empId varchar(50) not null, investment80C  double(9,2), medical80D double(9,2), interestHomeLoan double(9,2), investment80E double(9,2), donations80G double(9,2), savings80TTA double(9,2), interest80EE double(9,2), investment80U double(9,2), ltaPaid double(9,2), rentPaid double(9,2), foreign key(empId) references emp_master(empId));

insert into EMP_MASTER(empId,empName,email,age,location,regime)
values('b0218995', 'Shashank Shekhar', 'ssr3151@gmail.com', 21, 'rural', 'old');

insert into INCOME_MASTER(empId,basicSalary,pf,gratuity,medical,hra,lta,specialAllowances,incomeOtherSources)
values('b0218995', 540000, 64800, 25974, 3821, 270000, 0, 445405, 0);

insert into DEDUCTION_MASTER(empId,investment80C,medical80D,interestHomeLoan,investment80E,donations80G,savings80TTA,interest80EE,investment80U,ltaPaid,rentPaid)
values('b0218995', 150000, 0, 0, 0, 0, 0, 0, 75000, 0, 135000);

select * from EMP_MASTER;

select * from DEDUCTION_MASTER D Left Join EMP_MASTER E On D.empId = E.empId;

select * from INCOME_MASTER D Left Join EMP_MASTER E On D.empId = E.empId;