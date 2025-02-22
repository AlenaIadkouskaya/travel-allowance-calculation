package pl.iodkovskaya.travel_allowance_calculation.logic.travel.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.iodkovskaya.travel_allowance_calculation.logic.diet.model.dto.DietDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.overnightStay.exception.OvernightStayException;
import pl.iodkovskaya.travel_allowance_calculation.logic.overnightStay.model.dto.OvernightStayDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.transport.model.dto.TransportCostDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.model.dto.TravelRequestDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.model.dto.TravelResponseDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.travel.repository.TravelRepository;
import pl.iodkovskaya.travel_allowance_calculation.logic.user.model.dto.UserRequestDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.user.repository.UserRepository;
import pl.iodkovskaya.travel_allowance_calculation.logic.user.service.UserService;
import pl.iodkovskaya.travel_allowance_calculation.logic.user.service.UserReaderService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TravelServiceImplTest {
    private final Long PESEL = 90010101001L;
    private final String FIRST_NAME = "name";
    private final String SECOND_NAME = "surname";
    private final String POSITION = "position";

    private final String CITY_FROM = "cityFrom";
    private final String CITY_TO = "cityTo";
    private final LocalDate START_DAY = LocalDate.now();
    private final LocalTime START_TIME = LocalTime.of(0, 0);

    //    private final BigDecimal PERCENT_25 = new BigDecimal(0.25);
    private final BigDecimal PERCENT_50 = BigDecimal.valueOf(0.5);//new BigDecimal(0.5);
    private final BigDecimal DAILY_ALLOWANCE = new BigDecimal(45);
    private final BigDecimal HALF_DAILY_ALLOWANCE = DAILY_ALLOWANCE.multiply(PERCENT_50);
    private final BigDecimal ZERO_DAILY_ALLOWANCE = BigDecimal.ZERO;
    private final BigDecimal ONE_NIGHT_WITHOUT_INVOICE = DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(1.5));
    private final BigDecimal ONE_NIGHT_WITH_INVOICE = BigDecimal.valueOf(100);
    private final BigDecimal MAX_AMOUNT_FOR_ONE_NIGHT_WITH_INVOICE = DAILY_ALLOWANCE.multiply(BigDecimal.valueOf(20));
    private final BigDecimal ADVANCE_PAYMENT = BigDecimal.valueOf(50);
    private final Boolean IS_INVOICE_AMOUNT_GREATER_ALLOWED = false;
    private final BigDecimal OTHER_EXPENSES_ZERO = new BigDecimal(0);


    @Autowired
    private TravelService travelService;

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserReaderService userReaderService;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        travelRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void should_calculate_expenses_for_short_trip_less_than_8_hours() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now();
        LocalTime endTime = LocalTime.of(7, 59);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);

        TransportCostDto transportCostDto = new TransportCostDto(0, BigDecimal.ZERO,
                "without", BigDecimal.ZERO, 0L, 0L, 0L, 0L);

        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES_ZERO, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(ZERO_DAILY_ALLOWANCE.subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_trip_more_8_and_less_12_hour() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now();
        LocalTime endTime = LocalTime.of(11, 59);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);
        TransportCostDto transportCostDto = new TransportCostDto(0, BigDecimal.ZERO,
                "without", BigDecimal.ZERO, 0L, 0L, 0L, 0L);
        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES_ZERO, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(HALF_DAILY_ALLOWANCE.subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_long_trip_more_than_12_hours() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now();
        LocalTime endTime = LocalTime.of(23, 59);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);
        TransportCostDto transportCostDto = new TransportCostDto(0, BigDecimal.ZERO,
                "without", BigDecimal.ZERO, 0L, 0L, 0L, 0L);
        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES_ZERO, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_long_trip_more_than_12_hours_with_food() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now();
        LocalTime endTime = LocalTime.of(23, 59);

        Integer numberOfBreakfasts = 1;
        Integer numberOfLunches = 1;
        Integer numberOfDinners = 1;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);
        TransportCostDto transportCostDto = new TransportCostDto(0, BigDecimal.ZERO,
                "without", BigDecimal.ZERO, 0L, 0L, 0L, 0L);
        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES_ZERO, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(ZERO_DAILY_ALLOWANCE.subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_trip_more_than_one_day_and_less_than_8_hours() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(7, 59);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);
        TransportCostDto transportCostDto = new TransportCostDto(0, BigDecimal.ZERO,
                "without", BigDecimal.ZERO, 0L, 0L, 0L, 0L);
        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES_ZERO, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(HALF_DAILY_ALLOWANCE).subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_trip_48_hours() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalTime startTime = LocalTime.of(9, 0);
        LocalDate endDay = LocalDate.now().plusDays(2);
        LocalTime endTime = LocalTime.of(9, 0);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);
        TransportCostDto transportCostDto = new TransportCostDto(0, BigDecimal.ZERO,
                "without", BigDecimal.ZERO, 0L, 0L, 0L, 0L);
        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, startTime, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES_ZERO, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(DAILY_ALLOWANCE).subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_trip_2_days_and_30_minutes() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now().plusDays(2);
        LocalTime endTime = LocalTime.of(0, 30);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);
        TransportCostDto transportCostDto = new TransportCostDto(0, BigDecimal.ZERO,
                "without", BigDecimal.ZERO, 0L, 0L, 0L, 0L);
        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES_ZERO, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(DAILY_ALLOWANCE).subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_trip_2_days_and_less_than_8_hours() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now().plusDays(2);
        LocalTime endTime = LocalTime.of(7, 59);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);
        TransportCostDto transportCostDto = new TransportCostDto(0, BigDecimal.ZERO,
                "without", BigDecimal.ZERO, 0L, 0L, 0L, 0L);
        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES_ZERO, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(DAILY_ALLOWANCE.subtract(ADVANCE_PAYMENT))
                .add(HALF_DAILY_ALLOWANCE));
    }

    @Test
    void should_calculate_expenses_for_trip_2_days_and_more_than_8_hours() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now().plusDays(2);
        LocalTime endTime = LocalTime.of(8, 0);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);
        TransportCostDto transportCostDto = new TransportCostDto(0, BigDecimal.ZERO,
                "without", BigDecimal.ZERO, 0L, 0L, 0L, 0L);
        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES_ZERO, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(DAILY_ALLOWANCE.subtract(ADVANCE_PAYMENT))
                .add(DAILY_ALLOWANCE));
    }

    @Test
    void should_calculate_expenses_for_trip_more_than_one_day_and_less_than_8_hours_with_food() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(7, 59);

        Integer numberOfBreakfasts = 1;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 1;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);
        TransportCostDto transportCostDto = new TransportCostDto(0, BigDecimal.ZERO,
                "without", BigDecimal.ZERO, 0L, 0L, 0L, 0L);
        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES_ZERO, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_trip_more_than_one_day_and_more_than_8_hours() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(8, 0);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);
        TransportCostDto transportCostDto = new TransportCostDto(0, BigDecimal.ZERO,
                "without", BigDecimal.ZERO, 0L, 0L, 0L, 0L);
        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES_ZERO, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(DAILY_ALLOWANCE).subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_trip_more_than_one_day_and_more_than_8_hours_with_food() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(8, 0);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 1;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);
        TransportCostDto transportCostDto = new TransportCostDto(0, BigDecimal.ZERO,
                "without", BigDecimal.ZERO, 0L, 0L, 0L, 0L);
        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES_ZERO, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(HALF_DAILY_ALLOWANCE).subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_trip_more_than_one_day_and_less_than_8_hours_with_one_night_without_invoice() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(7, 59);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 1;
        Integer inputQuantityOfOvernightStayWithInvoice = 0;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = BigDecimal.ZERO;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);
        TransportCostDto transportCostDto = new TransportCostDto(0, BigDecimal.ZERO,
                "without", BigDecimal.ZERO, 0L, 0L, 0L, 0L);
        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES_ZERO, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(HALF_DAILY_ALLOWANCE.subtract(ADVANCE_PAYMENT))
                .add(ONE_NIGHT_WITHOUT_INVOICE));
    }

    @Test
    void should_calculate_expenses_for_trip_2_days_and_more_than_8_hours_with_one_night_with_invoice() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now().plusDays(2);
        LocalTime endTime = LocalTime.of(8, 0);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 1;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = ONE_NIGHT_WITH_INVOICE;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);
        TransportCostDto transportCostDto = new TransportCostDto(0, BigDecimal.ZERO,
                "without", BigDecimal.ZERO, 0L, 0L, 0L, 0L);
        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES_ZERO, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo(DAILY_ALLOWANCE.add(DAILY_ALLOWANCE)
                .add(DAILY_ALLOWANCE).add(ONE_NIGHT_WITH_INVOICE).subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_calculate_expenses_for_trip_2_days_and_more_than_8_hours_with_one_night_with_invoice_and_one_night_without_invoice_and_with_advancePayment() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now().plusDays(2);
        LocalTime endTime = LocalTime.of(9, 0);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 1;
        Integer inputQuantityOfOvernightStayWithInvoice = 1;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = ONE_NIGHT_WITH_INVOICE;

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);
        TransportCostDto transportCostDto = new TransportCostDto(0, BigDecimal.ZERO,
                "without", BigDecimal.ZERO, 0L, 0L, 0L, 0L);
        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES_ZERO, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThat(travelResponseDto.getTotalAmount()).isEqualByComparingTo((DAILY_ALLOWANCE.add(DAILY_ALLOWANCE)
                .add(DAILY_ALLOWANCE).add(ONE_NIGHT_WITH_INVOICE).add(ONE_NIGHT_WITHOUT_INVOICE)).subtract(ADVANCE_PAYMENT));
    }

    @Test
    void should_overnight_stay_exception_for_trip_3_nights_with_input_two_nights_with_invoice_and_two_nights_without_invoice_and_with_advancePayment() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalDate endDay = LocalDate.now().plusDays(3);
        LocalTime endTime = LocalTime.of(0, 0);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 2;
        Integer inputQuantityOfOvernightStayWithInvoice = 2;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = ONE_NIGHT_WITH_INVOICE.add(ONE_NIGHT_WITH_INVOICE);

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);
        TransportCostDto transportCostDto = new TransportCostDto(0, BigDecimal.ZERO,
                "without", BigDecimal.ZERO, 0L, 0L, 0L, 0L);
        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, START_TIME, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES_ZERO, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        Executable e = () -> travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThrows(OvernightStayException.class, e);
    }

    @Test
    @DisplayName("Should throw OvernightStayException for invoice amount exceeding limit with one night stay")
    void testOvernightStayExceptionForExceededInvoiceAmount() {
        //GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(PESEL, FIRST_NAME, SECOND_NAME, POSITION);
        userService.addUser(userRequestDto);

        LocalTime startTime = LocalTime.of(18, 0);
        LocalDate endDay = LocalDate.now().plusDays(1);
        LocalTime endTime = LocalTime.of(18, 0);

        Integer numberOfBreakfasts = 0;
        Integer numberOfLunches = 0;
        Integer numberOfDinners = 0;

        Integer inputQuantityOfOvernightStayWithoutInvoice = 0;
        Integer inputQuantityOfOvernightStayWithInvoice = 1;
        BigDecimal amountOfTotalOvernightsStayWithInvoice = ONE_NIGHT_WITH_INVOICE.add(MAX_AMOUNT_FOR_ONE_NIGHT_WITH_INVOICE);

        DietDto dietDto = new DietDto(DAILY_ALLOWANCE, numberOfBreakfasts, numberOfLunches, numberOfDinners);

        OvernightStayDto overnightStayDto = new OvernightStayDto(inputQuantityOfOvernightStayWithoutInvoice, inputQuantityOfOvernightStayWithInvoice,
                amountOfTotalOvernightsStayWithInvoice, IS_INVOICE_AMOUNT_GREATER_ALLOWED);
        TransportCostDto transportCostDto = new TransportCostDto(0, BigDecimal.ZERO,
                "without", BigDecimal.ZERO, 0L, 0L, 0L, 0L);
        TravelRequestDto travelRequestDto = new TravelRequestDto(PESEL, CITY_FROM, CITY_TO, START_DAY, startTime, endDay,
                endTime, ADVANCE_PAYMENT, OTHER_EXPENSES_ZERO, dietDto, overnightStayDto, transportCostDto);

        //WHEN
        Executable e = () -> travelService.calculateTravelExpenses(travelRequestDto);

        //THEN
        assertThrows(OvernightStayException.class, e);
    }
}