package com.pizzaria.repository;

import com.pizzaria.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long>  {


}
