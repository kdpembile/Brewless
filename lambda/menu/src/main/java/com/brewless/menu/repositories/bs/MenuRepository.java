package com.brewless.menu.repositories.bs;

import com.brewless.menu.models.bs.Menu;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MenuRepository extends ReactiveMongoRepository<Menu, ObjectId> {

}
