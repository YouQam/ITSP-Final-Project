
import java.io.IOException;
import java.awt.Color;

import draw.*;
import shapes.*;
public class ReadCities{

	public static void main(String[] args)
	{
		//declaring path of the data, cities and point of GPS coordinates of each city 
		String path = "./CapitalCities.txt";
		City cities [] = new City[11];
		Point cityPoints [] = new Point[11];
		String Filelines [];

		//create frame
		SimpleFrame frame = new SimpleFrame(1280,840);

		//Center point of the display window
		Point windowCenter = new Point();
		windowCenter.setX(500);
		windowCenter.setY(500);

		//Center point of the bounding box
		Point bBoxCenter = new Point();

		try{
			Filelines = InputStringReader.readFileAsArray(path);

    		for (int i = 1; i < Filelines.length; i++) {
				cities[i] = new City();
				
				String cityAttributes [] = Filelines[i].split(",");
				
				for (int j = 1; j < cityAttributes.length; j++) {
					String coords [] = cityAttributes[1].split(" ");
					double xcoord = Double.parseDouble(coords[1]);
					double ycoord = Double.parseDouble(coords[0]);
					cityPoints[i] = new Point();
					cityPoints[i].setX(xcoord);
					cityPoints[i].setY(ycoord);
					cities[i].setCoordinates(cityPoints[i]);
					
					double area = Double.parseDouble(cityAttributes[2]);
					int pop = Integer.parseInt(cityAttributes[3]);
					double popdens = Double.parseDouble(cityAttributes[4]);
					double foreignp = Double.parseDouble(cityAttributes[5]);
					double gdp = Double.parseDouble(cityAttributes[6]);
					
					cities[i].setName(cityAttributes[0]);
					cities[i].setArea(area);
					cities[i].setPopulation(pop);
					cities[i].setPopDensity(popdens);
					cities[i].setForeignpop(foreignp);
					cities[i].setGDP(gdp);
				}
			}

			//Variables for computing the bounding box of coordinates of cities((((mine))))
			double minX = cities[1].getCoordinates().getX();
			double maxX = cities[1].getCoordinates().getX();
			double minY = cities[1].getCoordinates().getY();
			double maxY = cities[1].getCoordinates().getY();
			
			for (int i = 1; i < Filelines.length; i++) {
				if (cities[i].getCoordinates().getX() > maxX) {
					maxX = cities[i].getCoordinates().getX();
				}
				if (cities[i].getCoordinates().getX() < minX) {
					minX = cities[i].getCoordinates().getX();
				}
				if (cities[i].getCoordinates().getY() > maxY) {
					maxY = cities[i].getCoordinates().getY();
				}
				if (cities[i].getCoordinates().getY() < minY) {
					minY = cities[i].getCoordinates().getY();
				}
			}

			//Adding padding to the bounding box 
			minX -= 3;
			maxX += 15;
			minY -= 10;
			maxY += 15;

			//Computing the scale factor
			double scale = 900/Math.max((minY-maxY),(maxX-minX));
			
			//Creating top left point of the bounding box
			Point topLeftPoint = new Point();
			topLeftPoint.setX(minX);
			topLeftPoint.setY(maxY);

			//Computing the center of the bounding box
			bBoxCenter.setX((topLeftPoint.getX()+(0.5*(maxX-minX)))*scale);
			bBoxCenter.setY((topLeftPoint.getY()+(0.5*(minY-maxY)))*scale);

			//Calculating normalization for the color value of the circles
			double popdensnorm [] = new double[11];
			double maxdensity = cities[1].getPopDensity();
			double mindensity = cities[1].getPopDensity();
			for (int i = 1; i < Filelines.length; i++) {
				if (cities[i].getPopDensity() < mindensity) {
					mindensity = cities[i].getPopDensity();
				}
				else if (cities[i].getPopDensity() > maxdensity) {
					maxdensity = cities[i].getPopDensity();
				}
			}

			maxdensity = maxdensity - mindensity;

			for (int i = 1; i < Filelines.length; i++) {
				popdensnorm[i] = cities[i].getPopDensity() - mindensity;
				popdensnorm[i] = popdensnorm[i]/maxdensity;
			}
			
			//Calculating the GDP per capita 
			double gdppercapita [] = new double[11];
			for (int i = 1; i < Filelines.length; i++) {
				gdppercapita[i] = (cities[i].getGDP()*1000000000)/cities[i].getPopulation();
			}

			//Applying the scaling to the coordinates by modifying the cuurent city objects
			for (int i = 1; i < Filelines.length; i++) {
				cities[i].getCoordinates().setX(cities[i].getCoordinates().getX()*scale);
				cities[i].getCoordinates().setY(cities[i].getCoordinates().getY()*scale);

				//Adding labels for clarification
				Label cityname = new Label();
				cityname.setText(cities[i].getName());
				cityname.setPosition(cities[i].getCoordinates());
				frame.addToPlot(cityname);

				//Setting circles
				Circle pd = new Circle();
				//pd.setPoint(cities[i].getCoordinates()); change in circle class to setpoint rather than settopleftpoint
				pd.setTopLeftpoint(cities[i].getCoordinates());
				pd.setDiameter(Math.round(cities[i].getArea()/4.5));
				pd.setStrokeColor(Color.BLACK);
				pd.setStrokeWidth(1);
				pd.setFillColor(Color.getHSBColor(0f, (float)popdensnorm[i], .89f));
				
				frame.addToPlot(pd);
				frame.addToPlot(cities[i].getCoordinates());
			}

			//Computing the amount of required for translation
			double xTranslation = windowCenter.getX() - bBoxCenter.getX();
			double yTranslation = windowCenter.getY() - bBoxCenter.getY();

			//Variable for drawing the bars in the diagrams 
			int t = 0;
			int x = 880;
			int y = 125;

			//Translating Cities' coordinates and fliping the map around the horizontal centerline 
			for (int i = 1; i < Filelines.length; i++) {
				cities[i].getCoordinates().setX(cities[i].getCoordinates().getX() + xTranslation);
				cities[i].getCoordinates().setY(cities[i].getCoordinates().getY() + yTranslation);
				cities[i].getCoordinates().setY(900 -(cities[i].getCoordinates().getY()));
				cities[i].getCoordinates().setStrokeColor(Color.BLACK);
				frame.addToPlot(cities[i].getCoordinates());
				
				//Creating points for the diagram of city population
				Point pnPoint = new Point();
				pnPoint.setX(x);
				pnPoint.setY(y+t);
				
				//Creating rectangle for the bars for the diagram of city population
				Rectangle pn = new Rectangle();
				pn.setTopLeftPoint(pnPoint);
				pn.setWidth(cities[i].getPopulation()/30000);
				pn.setHeight(20);
				pn.setFillColor(Color.GREEN);
				pn.setStrokeColor(Color.BLACK);
				pn.setStrokeWidth(1);
				frame.addToPlot(pn);

				t+=20;
				//Creating points for the diagram of the amount of foreign population in %
				Point pfPoint = new Point();
				pfPoint.setX(x);
				pfPoint.setY(y+t);
				
				//Creating rectangle for the bars for the diagram of the foreign population in %
				Rectangle pf = new Rectangle();
				pf.setTopLeftPoint(pfPoint);
				pf.setWidth((float)cities[i].getForeignpop());
				pf.setHeight(20);
				pf.setFillColor(Color.BLUE);
				pf.setStrokeColor(Color.BLACK);
				pf.setStrokeWidth(1);
				frame.addToPlot(pf);
				
				t+=20;
				//Creating points for the diagram of GDP per capita
				Point gdpPoint = new Point();
				gdpPoint.setX(x);
				gdpPoint.setY(y+t);
				
				//Creating rectangle for the bars for the diagram of GDP per capita
				Rectangle gdp = new Rectangle();
				gdp.setTopLeftPoint(gdpPoint);
				gdp.setWidth((float)gdppercapita[i]/1000);
				gdp.setHeight(20);
				gdp.setFillColor(Color.YELLOW);
				gdp.setStrokeColor(Color.BLACK);
				gdp.setStrokeWidth(1);
				frame.addToPlot(gdp);

				//Creating labels for the diagrams
				Point labelPoint = new Point();
				labelPoint.setX(820);
				labelPoint.setY(122+t);
				
				Label describeDiagram = new Label();
				describeDiagram.setPosition(labelPoint);
				describeDiagram.setText(cities[i].getName());
				frame.addToPlot(describeDiagram);
				
				Point gdpValues = new Point();
				gdpValues.setX(gdp.getTopLeftPoint().getX() + gdp.getWidth() + 15);
				gdpValues.setY(gdp.getTopLeftPoint().getY()+17);
				
				Label gdpValuesLabel = new Label();
				gdpValuesLabel.setPosition(gdpValues);
				gdpValuesLabel.setText(String.valueOf((Math.round(gdppercapita[i]))) + " \u20ac");
				frame.addToPlot(gdpValuesLabel);
				
				Point popValues = new Point();
				popValues.setX(pn.getTopLeftPoint().getX() +pn.getWidth() + 15);
				popValues.setY(pn.getTopLeftPoint().getY()+17);
				
				Label popValuesLabel = new Label();
				popValuesLabel.setPosition(popValues);
				popValuesLabel.setText(String.valueOf(cities[i].getPopulation()));
				frame.addToPlot(popValuesLabel);
				
				Point popFValues = new Point();
				popFValues.setX(pf.getTopLeftPoint().getX() + pf.getWidth() + 15);
				popFValues.setY(pf.getTopLeftPoint().getY()+17);
				
				Label popFValuesLabel = new Label();
				popFValuesLabel.setPosition(popFValues);
				popFValuesLabel.setText(String.valueOf(cities[i].getForeignpop()) + " %");
				frame.addToPlot(popFValuesLabel); 

				t = t + 25;
		}

			//draw features
			frame.drawAllFeature();

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		// Trasnparent color
		Color Tc =new Color(0f,0f,0f,.0f );
		
		// Point for bar frame
		Point barframepoint = new Point();
		barframepoint.setX(800);
		barframepoint.setY(112);

		Rectangle barframe = new Rectangle();
		barframe.setTopLeftPoint(barframepoint);
		barframe.setWidth(450);
		barframe.setHeight(670);
		barframe.setFillColor(Tc);
		barframe.setStrokeColor(Color.BLACK);
		barframe.setStrokeWidth(1);
		frame.addToPlot(barframe);

		// Point for circles frame
		Point circlesframepoint = new Point();
		circlesframepoint.setX(10);
		circlesframepoint.setY(112);

		Rectangle circlesframe = new Rectangle();
		circlesframe.setTopLeftPoint(circlesframepoint);
		circlesframe.setWidth(770);
		circlesframe.setHeight(670);
		circlesframe.setFillColor(Tc);
		circlesframe.setStrokeColor(Color.BLACK);
		circlesframe.setStrokeWidth(1);
		frame.addToPlot(circlesframe);

		//Points and labels for project title
		Point projectTitle = new Point();
		projectTitle.setX(603);
		projectTitle.setY(40);
		
		Label projectTitleLabel = new Label();
		projectTitleLabel.setText("Final Java Project");
		projectTitleLabel.setPosition(projectTitle);
		frame.addToPlot(projectTitleLabel);

		//Points and label for project developer
		Point projectdev = new Point();
		projectdev.setX(600);
		projectdev.setY(55);
		
		Label projectdevLabel = new Label();
		projectdevLabel.setText("by: Yousef Qamaz");
		projectdevLabel.setPosition(projectdev);
		frame.addToPlot(projectdevLabel);
		
		//Point and label for title of circles
		Point circleTitlePoint = new Point();
		circleTitlePoint.setX(22);
		circleTitlePoint.setY(108);
		
		Label circleTitleLabel = new Label();
		circleTitleLabel.setText("The circles represent the areas of each city and the color saturation represents the population density.");
		circleTitleLabel.setPosition(circleTitlePoint);
		frame.addToPlot(circleTitleLabel);

		//Points and labels for title of the bar
		Point barTitlePoint = new Point();
		barTitlePoint.setX(812);
		barTitlePoint.setY(108);
		
		Label barTitleLabel = new Label();
		barTitleLabel.setText("The bars represent population, foreign residents and GDP per capita.");
		barTitleLabel.setPosition(barTitlePoint);
		frame.addToPlot(barTitleLabel);

		//Adding Lengend
		int lengendX = 1100, lengendY = 450;

		//Creating point for the legned of city population
		Point pnLegendPoint = new Point();
		pnLegendPoint.setX(lengendX);
		pnLegendPoint.setY(lengendY);
		
		//Creating rectangle for the legend of city population
		Rectangle pnLegendR = new Rectangle();
		pnLegendR.setTopLeftPoint(pnLegendPoint);
		pnLegendR.setWidth(15);
		pnLegendR.setHeight(15);
		pnLegendR.setFillColor(Color.GREEN);
		pnLegendR.setStrokeColor(Color.BLACK);
		pnLegendR.setStrokeWidth(1);
		frame.addToPlot(pnLegendR);

		//Creating point for the legned of foreign population
		Point pfLegendPoint = new Point();
		pfLegendPoint.setX(lengendX);
		pfLegendPoint.setY(lengendY + 25);
				
		//Creating rectangle for the legend of the foreign population
		Rectangle pfLegendR = new Rectangle();
		pfLegendR.setTopLeftPoint(pfLegendPoint);
		pfLegendR.setWidth(15);
		pfLegendR.setHeight(15);
		pfLegendR.setFillColor(Color.BLUE);
		pfLegendR.setStrokeColor(Color.BLACK);
		pfLegendR.setStrokeWidth(1);
		frame.addToPlot(pfLegendR);

		//Creating point for the legned of GDP per capita
		Point gdpLegendPoint = new Point();
		gdpLegendPoint.setX(lengendX);
		gdpLegendPoint.setY(lengendY + 50);
		
		//Creating rectangle for the legend of GDP per capita
		Rectangle gdpLegendR = new Rectangle();
		gdpLegendR.setTopLeftPoint(gdpLegendPoint);
		gdpLegendR.setWidth(15);
		gdpLegendR.setHeight(15);
		gdpLegendR.setFillColor(Color.YELLOW);
		gdpLegendR.setStrokeColor(Color.BLACK);
		gdpLegendR.setStrokeWidth(1);
		frame.addToPlot(gdpLegendR);

		//Point and label for PN legend
		Point pnPoint = new Point();
		pnPoint.setX(lengendX+33);
		pnPoint.setY(lengendY+14);
		
		Label pnLabel = new Label();
		pnLabel.setText("Population density");
		pnLabel.setPosition(pnPoint);
		frame.addToPlot(pnLabel);

		//Point and label for PF legend
		Point pfPoint = new Point();
		pfPoint.setX(lengendX+33);
		pfPoint.setY(lengendY+39);
		
		Label pfLabel = new Label();
		pfLabel.setText("Foreign residents");
		pfLabel.setPosition(pfPoint);
		frame.addToPlot(pfLabel);

		//Point and label for GDP legend
		Point gdpPoint = new Point();
		gdpPoint.setX(lengendX+33);
		gdpPoint.setY(lengendY+64);
		
		Label gdpLabel = new Label();
		gdpLabel.setText("GDP");
		gdpLabel.setPosition(gdpPoint);
		frame.addToPlot(gdpLabel);

		// Point for legend frame
		Point legendframepoint = new Point();
		legendframepoint.setX(lengendX-10);
		legendframepoint.setY(lengendY-10);

		Rectangle legendframe = new Rectangle();
		legendframe.setTopLeftPoint(legendframepoint);
		legendframe.setWidth(135);
		legendframe.setHeight(85);
		legendframe.setFillColor(Tc);
		legendframe.setStrokeColor(Color.BLACK);
		legendframe.setStrokeWidth(1);
		frame.addToPlot(legendframe);

		
		frame.drawAllFeature();
	}
}