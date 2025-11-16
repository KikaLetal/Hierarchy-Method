import java.util.*;

public class Calculator {
    public CalculationResult CalculateAll(Iterable<Iterable<Object>> table){
        CalculationResult result = new CalculationResult();

        int n = 0;
        for (Iterable<Object> row : table) {
            n++;
        }

        // 1. Весовой вектор
        result.weightingVector = CalcWeightingVector(table);
        System.out.println(result.weightingVector);

        // 2. Нормирующий множитель
        result.normalizingMultiplier = CalcNormalizingMultiplier(result.weightingVector);
        System.out.println(result.normalizingMultiplier);

        // 3. Приоритеты
        result.priorityVectors = CalcPriorityVectors(result.weightingVector, result.normalizingMultiplier);
        System.out.println(result.priorityVectors);

        // 4. Сумма столбцов
        result.columnSum = CalcColumnSum(table);
        System.out.println(result.columnSum);

        // 5. Пропорции предпочтений
        result.preference = CalcProportionalityOfPreferences(result.columnSum, result.priorityVectors);
        System.out.println(result.preference);

        // 6. Согласованность
        result.coherence = CalcCoherence(result.preference);
        System.out.println(result.preference);

        // 7. Индекс согласованности
        result.consistencyIndex = CalcConsistencyIndex(result.coherence, n);
        System.out.println(result.consistencyIndex);

        // 8. Индекс рандома
        result.RandomIndex = CalcRandIndex(n);
        System.out.println(result.RandomIndex);

        // 9. Консистентность
        result.consistency = CalcConsistency(result.consistencyIndex, result.RandomIndex);
        System.out.println(result.consistency);

        return result;
    }

    private List<Double> CalcWeightingVector(Iterable<Iterable<Object>> data){
        List<Double> result = new ArrayList<>();

        for(Iterable<Object> row : data){
            double product = 1.0;
            int count = 0;

            for(Object obj : row){
                if(obj == null) continue;

                double value;

                if(obj instanceof Number){
                    value = ((Number) obj).doubleValue();
                } else{
                    value = Double.parseDouble(obj.toString());
                }

                product *= value;
                count++;
            }

            if(count > 0){
                double geometrycMean = Math.pow(product, 1.0 / count);
                result.add(geometrycMean);
            } else{
                result.add(0.0);
            }
        }

        return  result;
    }

    private double CalcNormalizingMultiplier(List<Double> data){
        double result = 0;

        for(Double value : data){
            if(value != null)
                result += value;
        }

        return result;
    }

    private List<Double> CalcPriorityVectors(List<Double> WeightingVectors, double normalizingMultiplier){
        List<Double> result = new ArrayList<>();

        for(Double value : WeightingVectors){
            if (value == null) {
                result.add(0.0);
            } else {
                result.add(value / normalizingMultiplier);
            }
        }

        return result;
    }

    private List<Double> CalcColumnSum(Iterable<Iterable<Object>> data){
        List<Double> result = new ArrayList<>();

        for(Iterable<Object> row : data){
            int c = 0;
            for(Object obj : row) {
                if (obj == null) continue;

                double value;
                if (obj instanceof Number) {
                    value = ((Number) obj).doubleValue();
                } else {
                    value = Double.parseDouble(obj.toString());
                }

                if(result.size() <= c){
                    result.add(value);
                } else{
                    result.set(c, result.get(c) + value);
                }
                c++;
            }
        }

        return result;
    }

    private List<Double> CalcProportionalityOfPreferences(List<Double> Sums, List<Double> PriorityVectors){
        List<Double> result = new ArrayList<>();

        int n = Math.min(Sums.size(), PriorityVectors.size());

        for(int i = 0; i < n; i++){
            double value = PriorityVectors.get(i) * Sums.get(i);
            result.add(value);
        }

        return result;
    }

    private double CalcCoherence(List<Double> preference){
        double result = 0.0;

        for(Double value : preference){
            if(value != null){
                result += value;
            }
        }

        return result;
    }

    private double CalcConsistencyIndex(double coherence, int n){
        if (n <= 1) return 0;

        return (coherence - n) / (n - 1);
    }

    private double CalcRandIndex(int n){
        switch (n) {
            case 1: return 0;
            case 2: return 0;
            case 3: return 0.58;
            case 4: return 0.90;
            case 5: return 1.12;
            case 6: return 1.24;
            case 7: return 1.32;
            case 8: return 1.41;
            case 9: return 1.45;
            case 10: return 1.49;
            default: return 1.49;
        }
    }

    private double CalcConsistency(double consistencyIndex, double RandomIndex){
        if (RandomIndex == 0) return 0;

        return consistencyIndex / RandomIndex;
    }
}
