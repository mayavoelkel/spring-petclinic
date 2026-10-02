/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OwnerTests {

	@Test
	void addPetAddsPersistedPet() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setId(5);
		pet.setName("Buddy");

		owner.addPet(pet);

		assertTrue(owner.getPets().contains(pet));
		assertEquals(1, owner.getPets().size());
	}

	@Test
	void addPetDoesNotAddDuplicatePet() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setId(5);
		pet.setName("Buddy");

		owner.addPet(pet);
		owner.addPet(pet);

		assertEquals(1, owner.getPets().size());
	}

	@Test
	void addPetIgnoresNull() {
		Owner owner = new Owner();

		owner.addPet(null);

		assertTrue(owner.getPets().isEmpty());
	}

	@Test
	void addPetAddsPersistedPetWhenOwnerHasUnsavedPet() {
		Owner owner = new Owner();
		Pet unsaved = new Pet();
		unsaved.setName("Draft");
		owner.addPet(unsaved);
		Pet persisted = new Pet();
		persisted.setId(5);
		persisted.setName("Buddy");

		owner.addPet(persisted);

		assertEquals(2, owner.getPets().size());
		assertTrue(owner.getPets().contains(persisted));
	}

	@Test
	void addPetDoesNotAddSecondCopyOfSamePersistedPet() {
		Owner owner = new Owner();
		Pet original = new Pet();
		original.setId(5);
		original.setName("Buddy");
		Pet copy = new Pet();
		copy.setId(5);
		copy.setName("Buddy");

		owner.addPet(original);
		owner.addPet(copy);

		assertEquals(1, owner.getPets().size());
		assertSame(original, owner.getPets().get(0));
	}

	@Test
	void addPetKeepsDifferentPersistedPets() {
		Owner owner = new Owner();
		Pet buddy = new Pet();
		buddy.setId(5);
		buddy.setName("Buddy");
		Pet rex = new Pet();
		rex.setId(6);
		rex.setName("Rex");

		owner.addPet(buddy);
		owner.addPet(rex);

		assertEquals(2, owner.getPets().size());
	}

	@Test
	void getPetByIdReturnsNullWhenOwnerHasNoSuchPet() {
		Owner owner = new Owner();
		Pet buddy = new Pet();
		buddy.setId(5);
		owner.addPet(buddy);

		assertNull(owner.getPet(99));
	}

	@Test
	void getPetByIdSkipsUnsavedPets() {
		Owner owner = new Owner();
		Pet unsaved = new Pet();
		unsaved.setName("Draft");
		owner.addPet(unsaved);

		assertNull(owner.getPet(1));
	}

	@Test
	void getPetByNameSkipsPetsWithoutName() {
		Owner owner = new Owner();
		Pet unnamed = new Pet();
		unnamed.setId(4);
		owner.addPet(unnamed);
		Pet buddy = new Pet();
		buddy.setId(5);
		buddy.setName("Buddy");
		owner.addPet(buddy);

		assertSame(buddy, owner.getPet("Buddy"));
	}

	@Test
	void getPetByNameIgnoresCase() {
		Owner owner = new Owner();
		Pet buddy = new Pet();
		buddy.setId(5);
		buddy.setName("Buddy");
		owner.addPet(buddy);

		assertSame(buddy, owner.getPet("bUDDY"));
	}

	@Test
	void getPetByNameCanIgnoreUnsavedPets() {
		Owner owner = new Owner();
		Pet unsaved = new Pet();
		unsaved.setName("Buddy");
		owner.addPet(unsaved);

		assertNull(owner.getPet("Buddy", true));
		assertSame(unsaved, owner.getPet("Buddy", false));
	}

}
