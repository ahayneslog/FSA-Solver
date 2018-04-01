
%%*************************************%%
%% Demo file illustrating some facts   %%
%% and rules in Prolog                 %%
%%*************************************%%

%%*************************************%%
%% FACTS                               %%
%%*************************************%%                        
student(john, 22, 123-45-6789).
student(jeff, 28, 198-45-1435).
student(jenn, 23, 343-45-4539).

studentInClass(123-45-6789, cs121, 1).
studentInClass(198-45-1435, cs130, 2).
studentInClass(198-45-1435, cs121, 2).
studentInClass(343-45-4539, cs130, 1).

%%*************************************%%
%% RULES                               %%
%%*************************************%% 

attendees(CourseNum,Name) :- 
    studentInClass(SSN, CourseNum, _),
    student(Name,_,SSN).

