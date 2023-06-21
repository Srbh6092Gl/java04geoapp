package com.srbh6092gl.geo;

import java.util.*;
import java.util.stream.Collectors;

public class GeoApp {
    public static void main(String[] args) {
        List<Country> countries = new ArrayList<>();
        /*
        add data to countries
         */
        List<City> cities = countries.stream().map(Country::getCities).flatMap(Collection::stream).collect(Collectors.toList());
        Map<String,List<Country>> continentMap = countries.stream().collect(Collectors.groupingBy(Country::getContinent));

        //1
        for (Map.Entry<String,List<Country>> entry: continentMap.entrySet()){
            Optional<City> city = entry.getValue().stream().map(Country::getCities).flatMap(Collection::stream).max(Comparator.comparingInt(City::getPopulation));
            city.ifPresent(city1 -> System.out.println("Most populated city of "+entry.getKey()+"is: "+city1));
        }

        //2
        List<City> capitals = new ArrayList<>();
        countries.forEach(country -> {
            try {
                capitals.add(getCityById(country.getCapital(),cities));
            } catch (Exception e) {
                System.out.println("No capital with id: "+ country.getCapital());
                throw new RuntimeException(e);
            }
        });
        capitals.stream().max(Comparator.comparingInt(City::getPopulation)).ifPresent(city -> System.out.println("Highest populated capital city: "+city));

        //3
        for (Map.Entry<String,List<Country>> entry: continentMap.entrySet()){
            List<City> capitalsContinents = new ArrayList<>();
            entry.getValue().forEach(country -> {
                try {
                    capitalsContinents.add(getCityById(country.getCapital(),cities));
                } catch (Exception e) {
                    System.out.println("No capital with id: "+ country.getCapital());
                    throw new RuntimeException(e);
                }
            });
            capitalsContinents.stream().max(Comparator.comparingInt(City::getPopulation)).ifPresent(city -> System.out.println("Highest populated capital city of "+entry.getKey()+": "+city));
        }

        //4
        List<Country> citySortedCountries = countries.stream().sorted((a,b)->b.getCities().size()-a.getCities().size()).collect(Collectors.toList());
        citySortedCountries.forEach(System.out::println);

        //5, 7
        countries.stream().filter(c-> c.getPopulation()!=0).sorted((a,b)-> {
            if((b.getPopulation()/b.getSurfaceArea()) - (a.getPopulation()-a.getSurfaceArea())>0)
                return -1;
            else return 1;
        });

        List<City> maxPopCities = countries.stream().max(Comparator.comparingInt(Country::getPopulation)).get().getCities();
        List<City> minPopCities = countries.stream().min(Comparator.comparingInt(Country::getPopulation)).get().getCities();

        //6
        countries.stream().forEach(country ->{
            country.getCities().parallelStream().max(Comparator.comparingInt(City::getPopulation)).ifPresent(System.out::println);
            country.getCities().parallelStream().min(Comparator.comparingInt(City::getPopulation)).ifPresent(System.out::println);
        });

        //8
        for (Map.Entry<String,List<Country>> entry: continentMap.entrySet()){
            entry.getValue().stream().max(Comparator.comparingDouble(Country::getGnp)).ifPresent(country -> System.out.println("Richest coutnry of "+entry.getKey()+" is "+country));
        }
        //9
        countries.stream().max(Comparator.comparingInt(Country::getPopulation)).ifPresent(country -> System.out.println("Max population of country: "+country.getPopulation()));
        countries.stream().min(Comparator.comparingInt(Country::getPopulation)).ifPresent(country -> System.out.println("Min population of country: "+country.getPopulation()));
        int totPopulation = countries.stream().map(Country::getPopulation).reduce(Integer::sum).get();
        System.out.println("Avg population: "+(totPopulation/countries.size()));
        //10
        for (Map.Entry<String,List<Country>> entry: continentMap.entrySet()){
            entry.getValue().stream().max(Comparator.comparingInt(Country::getPopulation)).ifPresent(country -> System.out.println("Max population of country of "+entry.getKey()+ " is: "+country.getPopulation()));
            entry.getValue().stream().min(Comparator.comparingInt(Country::getPopulation)).ifPresent(country -> System.out.println("Min population of country of "+entry.getKey()+ " is: "+country.getPopulation()));
            int total = entry.getValue().stream().map(Country::getPopulation).reduce(Integer::sum).get();
            System.out.println("Avg population of "+entry.getKey()+" is "+(total/entry.getValue().size()));
        }
        //11
        countries.stream().max(Comparator.comparingInt(Country::getPopulation)).ifPresent(country -> System.out.println("Max population country: "+country));
        countries.stream().min(Comparator.comparingInt(Country::getPopulation)).ifPresent(country -> System.out.println("Min population country: "+country));
        //12
        for (Map.Entry<String,List<Country>> entry: continentMap.entrySet()){
            entry.getValue().stream().max(Comparator.comparingInt(Country::getPopulation)).ifPresent(country -> System.out.println("Max population country of "+entry.getKey()+ " is: "+country));
            entry.getValue().stream().min(Comparator.comparingInt(Country::getPopulation)).ifPresent(country -> System.out.println("Min population country of "+entry.getKey()+ " is: "+country));
        }
        //13
        for (Map.Entry<String,List<Country>> entry: continentMap.entrySet()){
            entry.getValue().stream().sorted(Comparator.comparingInt(country -> country.getCities().size())).forEach(System.out::println);
        }
        //14
        cities.stream().max(Comparator.comparingInt(City::getPopulation)).ifPresent(city -> System.out.println("Max populated city: "+city));
        cities.stream().min(Comparator.comparingInt(City::getPopulation)).ifPresent(city -> System.out.println("Min populated city: "+city));

        //15
        countries.stream().min(Comparator.comparingDouble(Country::getGnp)).ifPresent(country -> System.out.println("Min GNP: "+country));
        countries.stream().max(Comparator.comparingDouble(Country::getGnp)).ifPresent(country -> System.out.println("Max GNP: "+country));
        double totGNP = countries.stream().map(Country::getGnp).reduce(Double::sum).get();
        System.out.println("Avg GNP: "+(totGNP/ countries.size()));
        double sqSum = countries.stream().map(country -> Math.pow(country.getGnp(), 2)).reduce(Double::sum).get();
        System.out.println("Standard deviation GNP: "+(sqSum/ countries.size()));
    }

    private static City getCityById(int id, List<City> cities) throws Exception {
         Optional<City> capital = cities.stream().filter(city -> id== city.getId()).findFirst();
         if(capital.isPresent())
             return capital.get();
         throw new Exception("No city with id: "+ id);
    }
}
