import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.io.File;
import javax.swing.JOptionPane;


/**
 * Copyright (C) 2007  Fernando Meyer
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * A full copy of the license is available in gpl.txt and online at
 * http://www.gnu.org/licenses/gpl.txt
 */

public class CrossValidation implements Runnable {
    private Attribute [] attributesArray;
    //private DataInstance [] dataInstancesArray;

    private int folds = 1;

    private DataInstance [] testSet;
    private DataInstance [] trainingSet;
    private int numAnts;
    private double [][] pheromoneArray;
    private int numClasses;
    private int [][][] freqTij;
    private double [][] infoTij;
    private int [] freqT;
    private int [] control;
    private double [][] hArray;
    private double [][] probabilitiesArray;
    private boolean [][] unusableAttributeVsValueArray;
    private int minCasesRule;
    private int convergenceTest;
    private int numIterations;
    private int maxUncoveredCases;
    private GUIAntMinerJFrame caller;
    private boolean interrupted;
    private boolean pruneRule;  //boolean to turn pruning of rule on or off
    private double cPheromoneUpdate; //constant rate of pheromone update
    private double nParam; //paramter to be sent for parameterized heuristics
    private boolean pheromoneUpdateType; //True for nomalized, False for constant
    private int nQualityChoice;
    private int nRefinementChoice;
    private Thread cvThread;

    private double[] accuracyRate = new double[2];

    public double getnParam() {
        return nParam;
    }

    public double getcPheromoneUpdate() {
        return cPheromoneUpdate;
    }

    public int getNumAnts() {
        return numAnts;
    }

    public int getNQualityChoice(){
        return nQualityChoice;
    }

    public int getNRefinementChoice(){
        return nRefinementChoice;
    }

    public int getFolds() {
        return folds;
    }

    public int getMinCasesRule() {
        return minCasesRule;
    }

    public int getConvergenceTest() {
        return convergenceTest;
    }

    public int getNumIterations() {
        return numIterations;
    }

    public int getMaxUncoveredCases() {
        return maxUncoveredCases;
    }

    public boolean isPruneRule() {
        return pruneRule;
    }

    public boolean isPheromoneUpdateType() {
        return pheromoneUpdateType;
    }

    public CrossValidation(GUIAntMinerJFrame caller){
        this.caller = caller;
        interrupted = false;
        accuracyRate[0] = -1;
    }

    public void setAttributesArray(Attribute [] attributesArray){
        this.attributesArray = attributesArray;
    }
    public void setNumAnts(int numAnts){
        this.numAnts = numAnts;
    }
    public void setFolds(int folds){
        this.folds = folds;
    }
    public void setMinCasesRule(int minCasesRule){
        this.minCasesRule = minCasesRule;
    }
    public void setConvergenceTest(int convergenceTest){
        this.convergenceTest = convergenceTest;
    }
    public void setNumIterations(int numIterations){
        this.numIterations = numIterations;
    }
    public void setMaxUncoveredCases(int maxUncoveredCases) {
        this.maxUncoveredCases = maxUncoveredCases;
    }
    public void setPruning(boolean pruneRule){
        this.pruneRule = pruneRule;
    }
    public void setPheromoneUpdateType(boolean pheromoneUpdateType){
        this.pheromoneUpdateType = pheromoneUpdateType;
    }
    public void setcPheromoneUpdate(double cPheromoneUpdate) {
        this.cPheromoneUpdate = cPheromoneUpdate;
    }
    public void setnParam(double nParam) {
        this.nParam = nParam;
    }
    public void setnQualityChoice(int nQualityChoice){
        this.nQualityChoice = nQualityChoice;
    }
    public void setnRefinementChoice(int nRefinementChoice){
        this.nRefinementChoice = nRefinementChoice;
    }

    public void start() {
        cvThread = new Thread(this);
        try {
            initialize();
            cvThread.start();
            //  caller.getJProgressBar1().setIndeterminate(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        cvThread = null;
        interrupted = true;
    }

    private void initialize() throws Exception{
        int n=0;
        int arraysSize;

        if(numAnts==0 || minCasesRule==0 ||
                convergenceTest==0 || numIterations==0 || maxUncoveredCases==0){
            caller.getJProgressBar1().setIndeterminate(false);
            caller.setIsClassifying(false);
            JOptionPane.showMessageDialog(null, "<html><font face=Dialog size=3>At least one of the parameters is invalid.</font></html>"
                    , "Error", JOptionPane.ERROR_MESSAGE);
            throw new InvalidArgumentException();
        }

        arraysSize = attributesArray.length - 1;

        numClasses = attributesArray[arraysSize].getTypes().length;

        freqT = new int[numClasses];

        pheromoneArray = new double[arraysSize][];
        freqTij = new int[arraysSize][][]; //freqTij[noOfAttributes][noOfValues][noOfClasses]
        infoTij = new double[arraysSize][];
        hArray = new double[arraysSize][];
        unusableAttributeVsValueArray = new boolean[arraysSize][];
        probabilitiesArray = new double[arraysSize][];

        for(n=0; n < arraysSize; n++){
            pheromoneArray[n] = new double[attributesArray[n].getTypes().length];
            freqTij[n] = new int[attributesArray[n].getTypes().length][numClasses];
            infoTij[n] = new double[attributesArray[n].getTypes().length];
            hArray[n] = new double[attributesArray[n].getTypes().length];
            unusableAttributeVsValueArray[n] = new boolean[attributesArray[n].getTypes().length];
            probabilitiesArray[n] = new double[attributesArray[n].getTypes().length];
        }
    }

    public void run(){
        Thread currentThread = Thread.currentThread();

        Random random = new Random();
        boolean goOn;

        double [][] pheromoneTempArray = new double[attributesArray.length-1][];


        List<Double> accuracyRatesList = new LinkedList<Double>();
        List<Double> numberOfTermsList = new LinkedList<Double>();
        List<Double> numberOfRulesList = new LinkedList<Double>();

        double totalTestAccuracyRate=0.0;
        double totalTrainingAccuracyRate=0.0;

        Date date = new Date();

        caller.getJTextArea1().setLineWrap(true);
        printHeader();

        group();


        // System.out.println("Cross Validation "+(crossValidation+1)+" of "+folds);

        Date date2 = new Date();


        // splitDataSet(1);
        DataInstance [] trainingSetClone = (DataInstance[])trainingSet.clone();

        int bestAntIndex=-1;
        List<Object> bestIterationAntsList = new ArrayList<Object>();
        List antsFoundRuleList = new ArrayList();

        while(trainingSet.length > maxUncoveredCases && currentThread == cvThread){
            //  System.out.print(".");
            bestAntIndex=0;

            initializePheromoneTrails();

            calculateFreqTij();
            calculateInfoTij();

            int iteration, deltaCount;
            iteration=deltaCount=0;

            while(iteration < numIterations && deltaCount < convergenceTest) {

                double bestQuality = 0;
                bestAntIndex = 0;

                Ant [] antsArray = new Ant[numAnts];
                for(int x=0; x < numAnts; x++){
                    antsArray[x] = new Ant(attributesArray.length-1);
                }

                for(int antIndex=0; antIndex < numAnts; antIndex++){
                    Ant currentAnt = antsArray[antIndex];

                    for(int n=0; n < pheromoneTempArray.length; n++)
                        pheromoneTempArray[n] = (double[]) pheromoneArray[n].clone();

                    //attDistinct[attribute i] contains the number of distinct values for attribute i
                    int [] attDistinctLeft = new int[attributesArray.length-1];
                    for(int i=0; i <attDistinctLeft.length; i++)
                        attDistinctLeft[i] = attributesArray[i].getTypes().length-1;

                    for(int i=0; i < unusableAttributeVsValueArray.length; i++)
                        for(int j=0; j < unusableAttributeVsValueArray[i].length; j++)
                            unusableAttributeVsValueArray[i][j] = false;

                    goOn = true;
                    while(goOn && currentThread == cvThread){
                        calculateHeuristicFunction(currentAnt);
                        calculateProbabilities(currentAnt, pheromoneTempArray);

                        float randomNumber = (random.nextInt() << 1 >>> 1) % 101;
                        randomNumber /= 100;
                        boolean found = false;
                        double sum = 0.0;
                        for(int i=0; i < probabilitiesArray.length; i++){
                            for(int j=0; j < probabilitiesArray[i].length && !found; j++){
                                sum += probabilitiesArray[i][j];
                                if(sum >= randomNumber){
                                    if(!ruleConstructor(currentAnt, i, j)){
                                        //set to true so that this ant does not try to use term ij again
                                        unusableAttributeVsValueArray[i][j] = true;
                                        attDistinctLeft[i]--;
                                        pheromoneTempArray[i][j] = 0.0;
                                    }else{
                                        attDistinctLeft[i] = -1;
                                    }
                                    found = true;
                                }
                            }
                        }

                        //determine if the ant already tried to use all the possible values of attribute a
                        for(int a=0; a < attDistinctLeft.length; a++){
                            if(attDistinctLeft[a] <= 0){
                                currentAnt.getMemory()[a] = 2;
                            }
                        }
                        goOn = false;
                        int a=0;
                        do{
                            if(currentAnt.getMemory()[a] == 0)
                                goOn = true;
                            a++;
                        }while(!goOn && a < currentAnt.getMemory().length);
                    }

                    determineRuleConsequent(currentAnt);
                    calculateRuleQuality(currentAnt, nParam, nQualityChoice, false);

                    //If prune Rule is set to true, then prune the rule
                    if(pruneRule) {
                        try {
                            currentAnt = pruneRule(currentAnt);
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                    antsArray[antIndex] = currentAnt;

                    if(currentAnt.getRuleQuality() >= bestQuality){
                        bestQuality = currentAnt.getRuleQuality();
                        bestAntIndex = antIndex;
                    }
                }

                try {
                    bestIterationAntsList.add(antsArray[bestAntIndex].clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }

                //check if rule quality has stagnated by comparing the last best quality with the previous one
                if(bestIterationAntsList.size() > 1){
                    if(((Ant) bestIterationAntsList.get(bestIterationAntsList.size()-1)).getRuleQuality() == ((Ant) bestIterationAntsList.get(bestIterationAntsList.size()-2)).getRuleQuality())
                        deltaCount++;
                    else
                        deltaCount = 0;
                }else
                    deltaCount++;

                //FLAG HERE IS FIXED AND NEEDS TO GO
                //pheromoneUpdateType = true;
                //cPheromoneUpdate = 0.5-0.95;
                updatePheromone(antsArray[bestAntIndex], pheromoneUpdateType, cPheromoneUpdate);

                iteration++;
            }


            //determine which ant has the best quality
            ListIterator li = bestIterationAntsList.listIterator();
            int index=0;
            bestAntIndex=0;
            double bestQuality = 0.0;
            while(li.hasNext()){
                Object temp = li.next();
                if(((Ant) temp).getRuleQuality() >= bestQuality){
                    bestQuality = ((Ant) temp).getRuleQuality();
                    bestAntIndex = index;
                }
                index++;
            }

            try {
                if (((Ant) bestIterationAntsList.get(bestAntIndex)).hasRules()) {
                    antsFoundRuleList.add(((Ant) bestIterationAntsList.get(bestAntIndex)).clone());
                }
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            //remove covered cases from the trainingSet
            int count=0;
            if(bestAntIndex != -1){
                li = ((Ant) bestIterationAntsList.get(bestAntIndex)).getInstancesIndexList().listIterator(((Ant) bestIterationAntsList.get(bestAntIndex)).getInstancesIndexList().size());
                while(li.hasPrevious()){
                    Object temp = li.previous();
                    trainingSet[((Integer) temp).intValue()] = null;
                    count++;
                }
            }
            DataInstance [] tempTrainingSet = new DataInstance[trainingSet.length-count];
            count=0;
            for(int x=0; x < trainingSet.length; x++){
                if(trainingSet[x] != null)
                    tempTrainingSet[count++] = trainingSet[x];
            }

            if (trainingSet.length == tempTrainingSet.length && !((Ant) bestIterationAntsList.get(bestAntIndex)).hasRules()) {
// we could not create a rule
                break;
            }

            trainingSet = tempTrainingSet;

            bestIterationAntsList.clear();
        }


        numberOfRulesList.add(new Double((double)(antsFoundRuleList.size()+1)));

        int sum=0;
        ListIterator li = antsFoundRuleList.listIterator();
        while(li.hasNext()){
            sum += ruleSize(((Ant) li.next()).getRulesArray());
        }
        numberOfTermsList.add(new Double((double)sum));


        //initializes freqT, which contains the number of cases that identify a class in the trainingSet
        for(int n=0; n < numClasses; n++){
            freqT[n] = 0;
        }
        int classIndex;
        int greatest=0, defaultClassIndex=0;
        for(int n=0; n < trainingSet.length; n++){
            classIndex = trainingSet[n].getValues()[trainingSet[n].getValues().length-1];
            freqT[classIndex]++;
            if(freqT[classIndex] > greatest){
                greatest = freqT[classIndex];
                defaultClassIndex = classIndex;
            }
        }

        double trainingAccuracyRate = calculateAccuracyRate(trainingSetClone, antsFoundRuleList, defaultClassIndex);
        totalTrainingAccuracyRate += trainingAccuracyRate;

        double testAccuracyRate = calculateAccuracyRate(testSet, antsFoundRuleList, defaultClassIndex);
        totalTestAccuracyRate += testAccuracyRate;

        accuracyRatesList.add(new Double(testAccuracyRate));

        for(ListIterator i=antsFoundRuleList.listIterator(); i.hasNext();){
            Object antObj = i.next();
            int [] rule = ((Ant)antObj).getRulesArray();
            // caller.getJTextArea1().append(getRuleString(rule, ((Ant)antObj).getRuleConsequent()) + "\n");
        }

        // System.out.println("\nAccuracy rate on the training set: "+trainingAccuracyRate+" %");
        //System.out.println("Accuracy rate on the test set:     "+testAccuracyRate+" %");


        //System.out.println("Time taken: "+((new Date().getTime() - date2.getTime())/1000.0)+" s.\n");



        if(!interrupted)

        {
            DecimalFormat myFormatter = new DecimalFormat("###.##");

            double total = 0.0;
            li = numberOfRulesList.listIterator();
            while (li.hasNext()) {
                total += ((Double) li.next()).doubleValue();
            }
            total = 0.0;
            li = numberOfTermsList.listIterator();
            while (li.hasNext()) {
                total += ((Double) li.next()).doubleValue();
            }

        }

        double sum1 = 0.0;
        for( int i = 0; i < accuracyRatesList.size(); i++ ) {
            sum1 += accuracyRatesList.get(i);
        }
        System.out.println(sum1 / accuracyRatesList.size());
        System.exit(0);

    }

    /**
     *
     */
    private void printHeader(){
        if(caller.getJCheckBox1IsSelected())
            caller.getJTextArea1().setText(null);
        caller.getJTextArea1().append("=== Run Information ===\n\n");
        caller.getJTextArea1().append("Relation:   " + caller.getJLabel2().getText() + "\n");
        //caller.getJTextArea1().append("Instances:  " + dataInstancesArray.length + "\n");
        caller.getJTextArea1().append("Attributes: " + attributesArray.length + "\n");
        for(int x=0; x < attributesArray.length; x++){
            caller.getJTextArea1().append("            " + attributesArray[x].getAttributeName() + "\n");
        }
        caller.getJTextArea1().append("\nUser-defined Parameters\n\n");
        caller.getJTextArea1().append("Folds:                 "+folds+"\n");
        caller.getJTextArea1().append("Number of Ants:        "+numAnts+"\n");
        caller.getJTextArea1().append("Min. Cases per Rule:   "+minCasesRule+"\n");
        caller.getJTextArea1().append("Max. uncovered Cases:  "+maxUncoveredCases+"\n");
        caller.getJTextArea1().append("Rules for Convergence: "+convergenceTest+"\n");
        caller.getJTextArea1().append("Number of Iterations:  "+numIterations+"\n");
    }

    /**
     * @param instancesArray
     * @param antsList
     * @param defaultClassIndex
     * @return
     */
    private double calculateAccuracyRate(DataInstance [] instancesArray, List antsList, int defaultClassIndex){
        int correctlyCovered = 0;
        ListIterator liAnt;
        boolean covering, classesCompared;

        for(int x=0; x < instancesArray.length; x++){
            liAnt = antsList.listIterator();
            classesCompared = false;
            while(liAnt.hasNext() && !classesCompared){
                Object antObj = liAnt.next();
                int [] rulesArray = ((Ant) antObj).getRulesArray();
                covering = true;
                for(int x2=0; x2 < rulesArray.length && covering; x2++){
                    if(rulesArray[x2] != -1)
                        if(rulesArray[x2] == instancesArray[x].getValues()[x2])
                            covering = true;
                        else
                            covering = false;
                }
                //if the rule covered the case, check if the rule consequent matches the class of the case
                if(covering){
                    if(instancesArray[x].getValues()[rulesArray.length] == ((Ant)antObj).getRuleConsequent())
                        correctlyCovered++;
                    classesCompared = true;
                    //if the case was not covered by any rule so far and there is only the default rule left,
                    //check if the case class matches the default rule consequent
                }else if(!liAnt.hasNext()){
                    if(instancesArray[x].getValues()[rulesArray.length] == attributesArray[attributesArray.length-1].getIntTypesArray()[defaultClassIndex])
                        correctlyCovered++;
                    classesCompared = true;
                }
            }
        }
        Double result = new Double(((double)correctlyCovered)/((double)instancesArray.length));
        if(Double.isNaN(result.doubleValue())){
            result = new Double(0);
        }
        return result.doubleValue()*100;
    }

    /**
     * @param valuesList
     * @param average
     * @param folds
     * @return
     */
    private double calculateVariance(List valuesList, double average, int folds){
        double calc = 0.0;
        ListIterator li = valuesList.listIterator();
        while(li.hasNext()){
            calc += Math.pow(((Double) li.next()).doubleValue() - average, 2.0);
        }
        calc /= folds - 1;
        calc /= folds;
        calc = Math.sqrt(calc);
        return calc;
    }

    /**
     * Assigns each case a number with a value between 0 and the number of cross-validation folds -1.
     */
    private void group(){

    }

    /**
     * Initializes trails with the same quantity of pheromone
     */
    private void initializePheromoneTrails(){
        int totalDistinct = totalDistinct();
        for(int n=0; n < pheromoneArray.length; n++){
            for(int n2=0; n2 < attributesArray[n].getTypes().length; n2++)
                pheromoneArray[n][n2] = log2(numClasses)/totalDistinct;
        }
    }

    /**
     * Initializes freqTij, which contains the number of cases that identify a class in the trainingSet.
     */
    private void calculateFreqTij(){
        for(int n=0; n < trainingSet.length; n++){
            int attIndex=0,attValueIndex,classIndex;
            for(int n2=0; n2 < trainingSet[n].getValues().length-1; n2++){
                attValueIndex = trainingSet[n].getValues()[n2];
                classIndex = trainingSet[n].getClassValue();
                if(attValueIndex > -1)
                    freqTij[attIndex][attValueIndex][classIndex]++;
                attIndex++;
            }
        }
    }

    /**
     * Initializes infoTij
     */
    private void calculateInfoTij(){
        for(int n=0; n < freqTij.length; n++){
            for(int n2=0; n2 < freqTij[n].length; n2++){
                int sum=0;
                double hw=0;
                for(int x=0; x < numClasses; x++)
                    sum += freqTij[n][n2][x];
                for(int x=0; x < numClasses; x++)
                    if(freqTij[n][n2][x] != 0 && sum !=0)
                        hw -= (double) freqTij[n][n2][x]/sum * log2((double) freqTij[n][n2][x]/sum);
                infoTij[n][n2] = hw;
            }
        }
    }

    /**
     * Calculates the heuristic function, given by:
     * hArray ->  hij = (log2 k - H(W|Ai = Vij)) / (S xm (S log2 k - H(W|Am = Vmn)))
     * @param ant
     */
    private void calculateHeuristicFunction(Ant ant){
        double sum=0.0;
        boolean termOccurs;
        int instanceClass;
        for(int c=0; c < attributesArray.length-1; c++){
            if(ant.getMemory()[c] == 0)  //if the attribute hasn't been used...
                for(int d=0; d < infoTij[c].length; d++)
                    sum += log2(numClasses) - infoTij[c][d];
        }
        for(int i=0; i < hArray.length; i++){
            for(int j=0; j < hArray[i].length; j++){
                if(!unusableAttributeVsValueArray[i][j]){
                    termOccurs = false;
                    //if all cases with term ij belong to the same class, then infoTij should be zero
                    instanceClass = trainingSet[0].getClassValue();
                    boolean isEqual = true;
                    for(int c=0; c < trainingSet.length && isEqual; c++){
                        if(trainingSet[c].getValues()[i] == attributesArray[i].getIntTypesArray()[j]){
                            termOccurs = true;
                            //compare the last instance class with the current instance class
                            if(instanceClass == trainingSet[c].getClassValue())
                                instanceClass = trainingSet[c].getClassValue();
                            else
                                isEqual = false;
                        }
                    }
                    if(!termOccurs)//if trainingSet does not contain term ij then...
                        hArray[i][j] = 0.0;
                    else if(isEqual)
                        hArray[i][j] = (log2(numClasses)) / sum;
                    else
                        hArray[i][j] = (log2(numClasses) - infoTij[i][j]) / sum;

                }else
                    hArray[i][j] = 0.0;
            }
        }
    }

    /**
     * Calculates the probability that each term ij be selected to constitute
     * part of the rule. The probability is given by:
     * Pij = nij * tij(t) / S xm * (S hmn * tmn(t))
     * @param ant
     * @param pheromoneArray
     */
    private void calculateProbabilities(Ant ant, double [][] pheromoneArray){
        double sum=0.0;
        int x=0, y=0;
        for(x=0; x < pheromoneArray.length; x++)
            if(ant.getMemory()[x] == 0) //if the attribute has not been used...
                for(y=0; y < hArray[x].length; y++)
                    sum += hArray[x][y] * pheromoneArray[x][y];
        for(x=0; x < probabilitiesArray.length; x++){
            for(y=0; y < probabilitiesArray[x].length; y++){
                if(ant.getMemory()[x] == 0){
                    double result = (hArray[x][y] * pheromoneArray[x][y]) / sum;
                    //if division by zero, attribute cannot be used
                    if(Double.isNaN(result)){
                        probabilitiesArray[x][y] = 0.0;
                        ant.getMemory()[x] = 2;
                    }else
                        probabilitiesArray[x][y] = result;
                }
                else
                    probabilitiesArray[x][y] = 0.0;
            }
        }
    }

    /**
     * Tries to add term ij to the rule. If the term is successfully added,
     * returns true, otherwise returns false.
     * @param ant
     * @param i
     * @param j
     * @return
     */
    private boolean ruleConstructor(Ant ant, int i, int j){
        int x;
        int total = 0;
        Object temp;
        List indexToRemoveList = new ArrayList();

        if(!ant.hasRules()){
            for(x=0; x < trainingSet.length; x++){
                if(trainingSet[x].getValues()[i] == attributesArray[i].getIntTypesArray()[j]){
                    ant.getInstancesIndexList().add(new Integer(x));
                    total++;
                }
            }
            if(total < minCasesRule){
                ant.clearRulesArray();
                ant.getInstancesIndexList().clear();
            }
        }else{
            ListIterator liInstancesIndex = ant.getInstancesIndexList().listIterator();
            while(liInstancesIndex.hasNext()){
                temp = liInstancesIndex.next();
                if(trainingSet[((Integer) temp).intValue()].getValues()[i] == attributesArray[i].getIntTypesArray()[j])
                    total++;
                else
                    indexToRemoveList.add(temp);
            }
        }

        if(total >= minCasesRule){
            ant.getRulesArray()[i] = j;
            ant.getMemory()[i] = 1; //cannot use attribute i to construct rule anymore
            ListIterator li = indexToRemoveList.listIterator();
            while(li.hasNext()){
                ant.getInstancesIndexList().remove(ant.getInstancesIndexList().indexOf(li.next()));
            }
            return true;
        }else
            return false;
    }


    /**
     * Determines the consequent value of the rule.
     * @param ant
     * @return
     */
    private int determineRuleConsequent(Ant ant){
        int ruleConsequent=-1;
        int [] freqClass = new int[numClasses];
        int trainingSetIndex;
        int trainingSetClass;

        ListIterator li = ant.getInstancesIndexList().listIterator();
        while(li.hasNext()){
            trainingSetIndex = ((Integer) li.next()).intValue();
            trainingSetClass = trainingSet[trainingSetIndex].getClassValue();
            freqClass[trainingSetClass]++;
        }

        int mostFreq = 0, mostFreqIndex = -1;
        for(int x=0; x < numClasses; x++){
            if(mostFreq < freqClass[x]){
                mostFreq = freqClass[x];
                mostFreqIndex = x;
            }
        }
        if(mostFreqIndex != -1)
            ruleConsequent = attributesArray[attributesArray.length-1].getIntTypesArray()[mostFreqIndex];
        ant.setRuleConsequent(ruleConsequent);
        return ruleConsequent;
    }

    /**
     * Calculates the rule quality of the ant.
     * @param ant
     * @param ruleRefinement
     * @return
     */
    protected double calculateRuleQuality(Ant ant, double nParam, int nQualityChoice, boolean ruleRefinement){   //, int nQualityChoice, int param - for F measure, cost measure, relative cost measure, Klosgen measure or m-estimate
        double quality = 0;
        int nTruePositive, nFalsePositive, nFalseNegative, nTrueNegative;
        int totalNumExamples = 0;
        nTruePositive=nFalsePositive=nFalseNegative=nTrueNegative=0;

        int antDataInstIndex=-1;
        ListIterator li=null;

        if(!ant.getInstancesIndexList().isEmpty()){
            li = ant.getInstancesIndexList().listIterator();
            antDataInstIndex = (Integer)li.next();
        }

        for(int dataInstanceIndex=0; dataInstanceIndex < trainingSet.length; dataInstanceIndex++){
            //if the data instance is covered by the rule...
            if(dataInstanceIndex == antDataInstIndex){
                if(li.hasNext())
                    antDataInstIndex = (Integer)li.next();
                if(trainingSet[dataInstanceIndex].getClassValue() == ant.getRuleConsequent())
                    nTruePositive++;
                else
                    nFalsePositive++;
            }
            else{   //If the data instance is not covered by the rule
                if(trainingSet[dataInstanceIndex].getClassValue() == ant.getRuleConsequent())
                    nFalseNegative++;
                else
                    nTrueNegative++;
            }
        }

        if(!ruleRefinement)
            quality = ruleQualityChoice(nTruePositive, nFalsePositive, nFalseNegative, nTrueNegative, nQualityChoice, nParam);
        else
            quality = ruleRefinementChoice(nTruePositive, nTrueNegative, nTruePositive + nFalseNegative, nFalsePositive + nTrueNegative, 1, nParam);
        if(Double.isNaN(quality))
            quality = 0.0;
        ant.setRuleQuality(quality);
        return quality;
    }

    /**
     * Depending on the choice, it calculate the rule quality of the ant
     * @param nTruePositive
     * @param nFalsePositive
     * @param nFalseNegative
     * @param nTrueNegative
     * @param nQualityChoice
     * @return
     */
    public double ruleQualityChoice(int nTruePositive, int nFalsePositive, int nFalseNegative, int nTrueNegative, int nQualityChoice, double nParam) {
        //int nParam
        double quality;
        //p =  nTruePositive; P = nTruePositive + nFalseNegative;
        //n = nTrueNegative;  N =  nFalsePositive + nTrueNegative;
        double P = nTruePositive + nFalseNegative;
        double N = nFalsePositive + nTrueNegative;
        double dSensitivity;
        double dSpecificity;

        if(P == 0.0)
            dSensitivity = 0.0;
        else
            dSensitivity = nTruePositive / P;
        if(N == 0.0)
            dSpecificity = 0.0;
        else
            dSpecificity = nTrueNegative / N;

        try {
            //All rules also consistent with Myra's implementation except Laplace and Cost Measure
            switch (nQualityChoice) {
                case 1:   //Sensitivity aka (True Positive rate or Recall) * Specificity (aka False Positive rate)       //Derived from Parpinelli paper
                    quality = dSensitivity * dSpecificity;    //dSensitivity * dSpecificity
                    break;
                case 2:  //Precision   p/p+n
                    quality = ((double) nTruePositive / (nTruePositive + nTrueNegative));
                    break;
                case 3:  //Laplace
                    quality = ((double) (nTruePositive + 1) / (nTruePositive + nTrueNegative + 2));
                    break;
                case 4: //Hider 3
                    quality = 2 * (P + N - nTrueNegative + nTruePositive + nParam ) ;
                    break;
                case 5: //Weighted Relative Accuracy  = Sensitivity - Specificity
                    quality = dSensitivity - dSpecificity;
                    break;
                case 6:    //AntMiner+  Precision + Coverage  p/p+n + p/P+N
                    quality = ((double) (nTruePositive / (nTruePositive + nTrueNegative)) + (nTruePositive / (P + N)));
                    break;
                case 7:  // Accuracy
                    quality = nTruePositive - nTrueNegative;
                    break;
                case 8:  //Correlation
                    quality = (nTruePositive*N - nTrueNegative*P) /
                              (Math.sqrt(P * N * (nTrueNegative + nTruePositive) * (P - nTruePositive + N - nTrueNegative)));
                    break;
                case 9: // cost measure c * p - (1 - c) * n
                    quality = (nParam * nTruePositive - (1 - nParam) * nTrueNegative);
                    break;
                case 10: // relative cost measure  cr * dSensitivity - 1 (1 - cr) * dSpecificity
                    quality = ((nParam * dSensitivity) - ((1 - nParam) * dSpecificity));
                    break;
                case 11: //Full Coverage
                    quality =  (nTruePositive + nTrueNegative) / (P + N);
                    break;
                case 12: // F-measure
                    double precision=((double) nTruePositive / (nTruePositive + nTrueNegative));
                    quality = (nParam * nParam + 1) * precision * dSensitivity / (nParam * nParam * precision + dSensitivity);
                    break;
                case 13: //m-estimate
                    quality = (((nTruePositive) + nParam * P / (P + N)) / (nTruePositive + nTrueNegative + nParam));
                    break;
                case 14:  //Klosgen measure
                    double coverage=((double) (nTruePositive + nTrueNegative) / (P + N));
                    precision=((double) nTruePositive / (nTruePositive + nTrueNegative));
                    quality = Math.pow(coverage, nParam) * (precision - (P / (P+N)));
                    break;
                case 15: //Jaccard rule based on Jaccard co-efficient
                    quality = (double) nTruePositive / (nTruePositive + nFalsePositive + nFalseNegative);
                    break;
                default:
                    quality = dSensitivity * dSpecificity;    //Sensitivity * Specificity
                    break;
            }
        }
        catch(Exception e)
        {
            quality = 0;
        }
        return quality;
    }
    public double ruleRefinementChoice(int nTruePositive, int nTrueNegative, int P, int N, int choice, double nParam) {
        double quality = 0;
        try {
            switch (choice) {
                case 1: //Inverted Precision
                    quality =  ((N - nTrueNegative) / (double)((P + N) - (nTruePositive + nTrueNegative)));
                    return quality;//return Double.isNaN(quality)? 0 : quality;
                case 2: //Inverted Laplace
                    double num = (double) (N - nTrueNegative + 1);
                    double denom = ((P + N) - (nTruePositive + nTrueNegative - 2));
                    quality = num / denom;
                    break;
                case 3: //Inverted m-estimate
                    num = (double) (N - nTrueNegative + nParam);
                    double num2 = ((double) P / (double) (P + N));
                    denom = (double) (P + N - (nTruePositive + nTrueNegative - nParam));
                    quality = num * num2 / denom;
                    break;
                default:
                    quality = ((double) (N - nTrueNegative) / (double) ((P + N) - (nTruePositive + nTrueNegative)));
            }
        }
        catch(Exception e)
        {
            quality = 0;
        }
        return quality;
    }

    /**
     * Updates the ant list that contains the indexes of the instances/cases covered
     * by the ant rule. This method must be run when the ant rule changes.
     */
    private void updateInstancesIndexList(Ant ant){
        ant.getInstancesIndexList().clear();
        boolean isCoveredByRule;

        for(int x=0; x < trainingSet.length; x++){
            isCoveredByRule=true;
            for(int y=0; y < trainingSet[x].getValues().length-1  && isCoveredByRule; y++)
                if(ant.getRulesArray()[y] != -1 && trainingSet[x].getValues()[y] != ant.getRulesArray()[y])
                    isCoveredByRule = false;
            if(isCoveredByRule)
                ant.getInstancesIndexList().add(new Integer(x));
        }
    }


    /**
     * Prunes the rule of the ant.
     * @param ant
     * @return
     * @throws CloneNotSupportedException
     */
    protected Ant pruneRule(Ant ant) throws CloneNotSupportedException{
        int numCond;
        double greatestQuality, currentRuleQuality;
        Ant antClone;
        Ant antCloneWithBestPrunedRule = ant;

        for(int a1=0; a1 < ant.getRulesArray().length; a1++){
            greatestQuality = currentRuleQuality = ant.getRuleQuality();
            numCond = 0;
            for(int a=0; a < ant.getRulesArray().length; a++){
                if(ant.getRulesArray()[a] != -1){
                    numCond++;
                    antClone = (Ant)ant.clone();
                    antClone.getRulesArray()[a] = -1;
                    updateInstancesIndexList(antClone);
                    determineRuleConsequent(antClone);
                    calculateRuleQuality(antClone, nParam, 1, false);
                    if(antClone.getRuleQuality() >= greatestQuality){
                        greatestQuality = antClone.getRuleQuality();
                        antCloneWithBestPrunedRule = (Ant) antClone.clone();
                    }
                }
            }
            if(greatestQuality >= currentRuleQuality && numCond > 1)
                ant = (Ant) antCloneWithBestPrunedRule.clone();
            else //rule could not be improved, so leave block
                a1 = ant.getRulesArray().length;
        }
        return ant;
    }

    /**
     * Updates the trail of pheromone.
     * @param ant
     */
    protected void updatePheromone(Ant ant, boolean normalizePheromoneOn, double cRate){
        //update pheromone for used terms
        for(int x=0; x < ant.getRulesArray().length; x++){
            if(ant.getRulesArray()[x] != -1){
                double currentValue = pheromoneArray[x][ant.getRulesArray()[x]];
                pheromoneArray[x][ant.getRulesArray()[x]] = currentValue + currentValue*(ant).getRuleQuality();
            }
        }
        //normalized rate
        if(normalizePheromoneOn) {
            double sum = 0;
            for (int x = 0; x < pheromoneArray.length; x++) {
                for (int y = 0; y < pheromoneArray[x].length; y++) {
                    sum += pheromoneArray[x][y];
                }
            }
            for (int x = 0; x < pheromoneArray.length; x++) {
                for (int y = 0; y < pheromoneArray[x].length; y++) {
                    pheromoneArray[x][y] /= sum;
                }
            }
        }
        else //Constant rate
        {
            for (int x = 0; x < pheromoneArray.length; x++) {
                for (int y = 0; y < pheromoneArray[x].length; y++) {
                    pheromoneArray[x][y] *= cRate;
                }
            }
        }
    }


    /**
     * Converts the rule from []int to a readable String
     * @param rule
     * @param ruleConsequent
     * @return the rule in String format
     */
    private String getRuleString(int [] rule, int ruleConsequent){
        String ruleStr;
        ruleStr = "IF ";
        return ruleStr;
    }

    private String getInstanceString(int [] instance){
        String instanceStr="";
        for(int x=0; x < instance.length; x++){
            if(instance[x] == -1)
                instanceStr += "?";
            else
                instanceStr += attributesArray[x].getTypes()[instance[x]];
            if(x < instance.length-1)
                instanceStr += ", ";
        }
        return instanceStr;
    }

    /**
     * Calculates the size of the rule.
     * @param rule
     * @return
     */
    private int ruleSize(int [] rule){
        int count=0;
        for(int x=0; x < rule.length; x++)
            if(rule[x] != -1)
                count++;
        return count;
    }

    /**
     * Returns the total number of possible attributes values
     * @return the total number of possible attributes values
     */
    public int totalDistinct(){
        int count=0;
        for(int n=0; n < attributesArray.length; n++){
            count += attributesArray[n].getTypes().length;
        }
        return count;
    }

    /**
     * @param d
     * @return
     */
    private double log2(double d) {
        return Math.log(d)/Math.log(2.0);
    }

    class InvalidArgumentException extends Exception {
        private static final long serialVersionUID = 1L;
        InvalidArgumentException() {
            super("At least one of the parameters is invalid.");
        }
        InvalidArgumentException(String msg) {
            super(msg);
        }
    }

    public static void main(String [] args)
    {

        CrossValidation cv = new CrossValidation(new GUIAntMinerJFrame());
        System.setProperty("java.awt.headless", "true");

// for(int i = 0; i < args.length; i++)
//    System.out.println(args[i]);
        //System.out.println(args[0].substring(1));
        MyFileReader myFileReader = new MyFileReader(new File(args[0]));
        Attribute[] attributesArray = null;
        DataInstance[] dataInstancesArray = null;
        if(myFileReader.fileIsOk()) {
            attributesArray = myFileReader.getAttributesArray();
            cv.trainingSet = myFileReader.getDataInstancesArray();

            myFileReader = new MyFileReader((new File(args[1])));

            if(myFileReader.fileIsOk())
                cv.testSet = myFileReader.getDataInstancesArray();
        }

        cv.setAttributesArray(attributesArray);
        // cv.setDataInstancesArray(cv.testSet);

        cv.setNumAnts(Integer.parseInt(args[2]));
        cv.setMinCasesRule(Integer.parseInt(args[4]));
        cv.setMaxUncoveredCases(Integer.parseInt(args[6]));
        cv.setConvergenceTest(Integer.parseInt(args[8]));
        cv.setNumIterations(Integer.parseInt(args[10]));
        cv.setnQualityChoice(1);
        cv.setnRefinementChoice(1);
        cv.setPheromoneUpdateType(true);
        cv.setPruning(true);
        cv.start();

    }


}


    