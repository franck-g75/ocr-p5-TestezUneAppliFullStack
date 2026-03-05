import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { SessionApiService } from './session-api.service';
import { HttpClientTestingModule, HttpTestingController,} from '@angular/common/http/testing';
import { Session } from '../interfaces/session.interface';

describe('SessionsApiService', () => {

  let service: SessionApiService;
  let httpMock: HttpTestingController;
  let pathService = 'api/session'; //pathService private

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientTestingModule],
      providers:[SessionApiService]
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // Vérifie qu’aucune requête HTTP inattendue n’est restée en suspens
    httpMock.verify(); //not a fonction ?
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call httpClient.delete on delete string string', () => {
    
    const id = '123';

    service.delete(id).subscribe();                    // Souscription au résultat (on ne s’intéresse pas à la valeur ici)
    const req = httpMock.expectOne(pathService + '/' + id);  // Expectation sur la requête HTTP générée
    expect(req.request.method).toBe('DELETE');
    
    req.flush(null);                                   // Simuler une réponse serveur (vide ou objet)

  });

  it('should call httpClient.create on create session', () => {
    
    const mockSession: Session = {
      id: 2,
      name: 'nom',
      description: 'session desciption',
      date: new Date(1975,4,30),
      teacher_id: 1,
      users: [1],
      createdAt: new Date(2025,1,24),
      updatedAt: new Date(2026,1,25)
    };

    service.create(mockSession).subscribe();                // Souscription au résultat
    const req = httpMock.expectOne(pathService);  // Expectation sur la requête HTTP générée
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockSession);

    req.flush(null);                                   // Simuler une réponse serveur (vide ou objet)

  });


  it('should call httpClient.put on update string session', () => {
    
    const mockSession: Session = {
      id: 2,
      name: 'nom',
      description: 'session desciption',
      date: new Date(1975,4,30),
      teacher_id: 1,
      users: [1],
      createdAt: new Date(2025,1,24),
      updatedAt: new Date(2026,1,25)
    };

    service.update((mockSession.id==undefined ? '' :  mockSession.id.toString()) , mockSession).subscribe();
    const req = httpMock.expectOne(pathService + '/' + (mockSession.id==undefined ? '' :  mockSession.id.toString()));  // Expectation sur la requête HTTP générée
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(mockSession);

    req.flush(null);                              // Simuler une réponse serveur (vide ou objet)

  });

  it('should call httpClient.post on participate string string', () => {
    
    const id = '123';
    const userId='456';

    service.participate(id,userId).subscribe();                    // Souscription au résultat (on ne s’intéresse pas à la valeur ici)
    const req = httpMock.expectOne(pathService + '/' + id + '/participate/' + userId);  // Expectation sur la requête HTTP générée
    expect(req.request.method).toBe('POST');
    
    req.flush(null);                                   // Simuler une réponse serveur (vide ou objet)

  });

  it('should call httpClient.delete on unParticipate string string', () => {
    
    const id = '123';
    const userId='456';

    service.unParticipate(id,userId).subscribe();                    // Souscription au résultat (on ne s’intéresse pas à la valeur ici)
    const req = httpMock.expectOne(pathService + '/' + id + '/participate/' + userId);  // Expectation sur la requête HTTP générée
    expect(req.request.method).toBe('DELETE');
    
    req.flush(null);                                   // Simuler une réponse serveur (vide ou objet)

  });




});


