
    close all;
	A=importdata('2014-01-27-13.57.15_36_-86.8_output_data.txt');
	A=A.data;
	min1 = min(A);
	max1 = max(A);
	
	x = min1(4):((max1(4)-min1(4))/40):max1(4);
	y = min1(3):((max1(3)-min1(3))/40):max1(3);
	
	[X,Y]=meshgrid(x,y);
	Z=griddata(A(:,4),A(:,3),log10(A(:,6)),X,Y);
	contourf(X,Y,Z,10);