package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.LocationDTO;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.LocationMapper;
import com.freelancemarketplace.backend.model.CityModel;
import com.freelancemarketplace.backend.model.CountryModel;
import com.freelancemarketplace.backend.model.LocationModel;
import com.freelancemarketplace.backend.repository.CityRepository;
import com.freelancemarketplace.backend.repository.CountryRepository;
import com.freelancemarketplace.backend.repository.LocationsRepository;
import com.freelancemarketplace.backend.service.LocationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImp implements LocationService {

    private final LocationsRepository locationsRepository;
    private final LocationMapper locationMapper;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    public LocationServiceImp(LocationsRepository locationsRepository, LocationMapper locationMapper, CountryRepository countryRepository, CityRepository cityRepository) {
        this.locationsRepository = locationsRepository;
        this.locationMapper = locationMapper;
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    public LocationDTO createLocation(LocationDTO locationDTO) {

        CountryModel country = checkCountry(locationDTO);

        CityModel city = checkCity(locationDTO);

        LocationModel locationEntity = new LocationModel();

        locationEntity.setCountry(country);
        locationEntity.setCity(city);
        locationEntity.setDetails(locationDTO.getDetails());

        locationsRepository.save(locationEntity);

        return locationMapper.toDTO(locationEntity);
    }

    @Override
    public LocationDTO updateLocation(Long locationId, LocationDTO locationDTO) {
        LocationModel locationModel = locationsRepository.findById(locationId).orElseThrow(
                ()->new ResourceNotFoundException("Location with id: " + " not found")
        );

        if(locationDTO.getCityName() != null)
            locationModel.setCity(checkCity(locationDTO));

        if(locationDTO.getCountryName() !=  null)
            locationModel.setCountry(checkCountry(locationDTO));

        if(locationDTO.getDetails() != null)
            locationModel.setDetails(locationDTO.getDetails());

        LocationModel savedLocation = locationsRepository.save(locationModel);

        return locationMapper.toDTO(savedLocation);
    }

    @Override
    public void deleteLocation(Long locationId) {
        LocationModel locationModel = locationsRepository.findById(locationId).orElseThrow(
                ()->new ResourceNotFoundException("Location with id:  " + locationId+ "not found")
        );

        locationsRepository.deleteById(locationId);
    }

    @Override
    public List<LocationDTO> getAll() {
        List<LocationModel> locations = locationsRepository.findAll();

        return locationMapper.toDTOs(locations);
    }

    CountryModel checkCountry(LocationDTO locationDTO){
        Optional<CountryModel> countryOpt = countryRepository.findByCountryName(locationDTO.getCountryName());

        CountryModel country;
        if(countryOpt.isPresent()){
            country = countryOpt.get();
        }else{
            country = new CountryModel();
            country.setCountryName(locationDTO.getCountryName());
            countryRepository.save(country);
        }
        return country;
    }


    CityModel checkCity(LocationDTO locationDTO){
        Optional<CityModel> cityOpt= cityRepository.findByCityName(locationDTO.getCityName());

        CityModel city;

        if(cityOpt.isPresent()){
            city = cityOpt.get();
        }else{
            city = new CityModel();
            city.setCityName(locationDTO.getCityName());
            cityRepository.save(city);
        }
        return city;
    }
}
