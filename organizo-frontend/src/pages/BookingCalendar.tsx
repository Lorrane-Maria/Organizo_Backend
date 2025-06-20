import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Calendar from 'react-calendar';
import { getAvailabilityByProfessional } from '../api';

export default function BookingCalendar() {
  const { professionalId, salonId, serviceId } = useParams<{
    professionalId: string;
    salonId: string;
    serviceId: string;
  }>();
  const [availableDates, setAvailableDates] = useState<Date[]>([]);
  const [date, setDate] = useState<Date | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    getAvailabilityByProfessional(Number(professionalId))
      .then(dates => setAvailableDates(dates));
  }, [professionalId]);

  const tileDisabled = ({ date: d }: { date: Date }) =>
    !availableDates.find(ad => ad.toDateString() === d.toDateString());

  const onSelectDate = (d: Date) => {
    setDate(d);
    navigate(`/book/review`, {
      state: { salonId, serviceId, professionalId, date: d.toISOString() }
    });
  };

  return (
    <main className="container">
      <h1>Escolha uma data</h1>
      <Calendar
        onClickDay={onSelectDate}
        tileDisabled={tileDisabled}
      />
    </main>
  );
}
