import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';
import { of } from 'rxjs/internal/observable/of';
import { HttpClientModule } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';

class MySessionServiceMock {
  participate = jest.fn().mockReturnValue(of(null));
  unParticipate = jest.fn().mockReturnValue(of(null));
  delete = jest.fn().mockReturnValue(of(null));
  create = jest.fn().mockReturnValue(of(null));
  update = jest.fn().mockReturnValue(of(null));
}

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should log out', () => {
    service.logOut();
    expect(service.isLogged).toEqual(false);
  });
  
  it('should log in', () => {
    let mySi: SessionInformation = {
      token: '123456789',
      type: 'qqchose',
      id: 2,
      username: 'fg',
      firstName: 'franck',
      lastName: 'guindeuil',
      admin: false
    };
    
    service.logIn(mySi);
    expect(service.isLogged).toEqual(true);
  
  });
  
  
});
















/*
describe('SessionService mocked', () => {
  let mySessionServiceMocked: MySessionServiceMock;

  beforeEach(() => {
    TestBed.configureTestingModule({
          imports: [
            RouterTestingModule,
            HttpClientModule
          ],
          providers: [
            { provide: SessionService, useClass: MySessionServiceMock}
          ]
        }).compileComponents();
    mySessionServiceMocked = TestBed.inject(SessionService) as unknown as MySessionServiceMock;
  });
  
  it('should call post on participate string string', () => {
    
    
    
  });

  it('should call ', () => {
    
    
    
  });

  it('should call ', () => {
    
    
    
  });

  it('should call ', () => {
    
    
    
  });

  it('should call ', () => {
    
    
    
  });

});
*/
