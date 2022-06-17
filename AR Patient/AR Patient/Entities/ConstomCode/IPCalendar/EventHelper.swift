//
//  EventHelper.swift
//  ShowOff
//
//  Created by Knoxweb on 04/11/19.
//

import EventKit

class EventHelper {
    /*let appleEventStore = EKEventStore()
    var calendars: [EKCalendar]?
    
    func generateEvent(title: String, startDate: Date, endDate: Date, notes: String) {
        let status = EKEventStore.authorizationStatus(for: EKEntityType.event)
        
        switch (status)
        {
        case EKAuthorizationStatus.notDetermined:
            // This happens on first-run
            requestAccessToCalendar(title: title, startDate: startDate, endDate: endDate, notes: notes)
        case EKAuthorizationStatus.authorized:
            // User has access
            print("User has access to calendar")
            //self.addAppleEvents(title: title, startDate: startDate, endDate: endDate, notes: notes)
        case EKAuthorizationStatus.restricted, EKAuthorizationStatus.denied:
            // We need to help them give us permission
            noPermission()
        }
    }
    
    func noPermission() {
        print("User has to change settings...goto settings to view access")
    }
    
    func requestAccessToCalendar(title: String, startDate: Date, endDate: Date, notes: String) {
        appleEventStore.requestAccess(to: .event, completion: { (granted, error) in
            if (granted) && (error == nil) {
                DispatchQueue.main.async {
                    print("User has access to calendar")
                    //self.addAppleEvents(title: title, startDate: startDate, endDate: endDate, notes: notes)
                }
            } else {
                DispatchQueue.main.async{
                    self.noPermission()
                }
            }
        })
    }*/
    
    func addAppleEvents(title: String, startDate: Date, endDate: Date, notes: String, job_id: String) {
        DispatchQueue.main.async {
            let store = EKEventStore()
            store.requestAccess(to: .event) {(granted, error) in
                if !granted { return }
                let event:EKEvent = EKEvent(eventStore: store)
                event.title = title
                event.startDate = startDate
                event.endDate = endDate
                event.notes = notes
                event.calendar = store.defaultCalendarForNewEvents
                
                do {
                    try store.save(event, span: .thisEvent)
                    print("events added with dates:")
                    let calendar_id = String(describing: event.eventIdentifier!)
                    if(calendar_id != "") {
                        //Global.appDelegate.registerEventAPICall(job_id: job_id, calendar_id: calendar_id)
                    }
                } catch let e as NSError {
                    print(e.description)
                    return
                }
                print("Saved Event => \(String(describing: event.eventIdentifier!))")
            }
        }
    }
    
    func removeEvent(eventID : String) {
        DispatchQueue.main.async {
            let store = EKEventStore()
            store.requestAccess(to: .event) {(granted, error) in
                if !granted { return }
                let eventToRemove = store.event(withIdentifier: eventID)
                if eventToRemove != nil {
                    do {
                        try store.remove(eventToRemove!, span: .thisEvent, commit: true)
                    } catch {
                        // Display error to user
                    }
                }
            }
        }
    }
}
