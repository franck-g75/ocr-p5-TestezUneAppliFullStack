import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';

import { MeComponent } from './me.component';
import { of } from 'rxjs/internal/observable/of';
import { SessionApiService } from 'src/app/features/sessions/services/session-api.service';
import { UserService } from 'src/app/services/user.service';

class MyUserServiceMock {
  delete = jest.fn().mockReturnValue(of(null));
  getById = jest.fn().mockReturnValue(of(null));
}

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let myUserServiceMock: MyUserServiceMock;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  const wbackSpy = jest.fn();

  beforeAll(() => {
    // On remplace la méthode réelle par le spy
    Object.defineProperty(window, 'history', {
      value: { back: wbackSpy },
      writable: true,
    });
    
  });

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useClass: MyUserServiceMock}
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    //fixture.detectChanges();
    myUserServiceMock = TestBed.inject(UserService) as unknown as MyUserServiceMock;

  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call window.history.back() when back() is invoked', () => {
    component.back();                    // appel de la méthode du composant
    expect(wbackSpy).toHaveBeenCalled(); // vérification du spy
  });

  it('should call this.sessionApiService.delete() when component.delete() is invoked', () => {
                 
      // 👉  Cast en `any` pour accéder à la méthode privée
      component.delete();
      expect(myUserServiceMock.delete).toHaveBeenCalledWith(mockSessionService.sessionInformation.id.toString());

  });
 
  it('should call this.sessionApiService.delete() when component.delete() is invoked', () => {
                 
      // 👉  Cast en `any` pour accéder à la méthode privée
      component.ngOnInit();
      expect(myUserServiceMock.getById).toHaveBeenCalledWith(mockSessionService.sessionInformation.id.toString());

  });

});
