import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { HttpTestingController } from '@angular/common/http/testing';

describe('UserService', () => {
  let service: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientModule],
      providers:[UserService]
    });
    service = TestBed.inject(UserService);

  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

 
});
