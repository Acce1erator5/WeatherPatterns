package uz.ttpu.weatherpatterns.mvi.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import uz.ttpu.weatherpatterns.data.api.FakeWeatherApi
import uz.ttpu.weatherpatterns.data.repository.WeatherRepository
import uz.ttpu.weatherpatterns.mvi.model.WeatherIntent

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherMviViewModelTest {
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun newViewModel() = WeatherMviViewModel(WeatherRepository(FakeWeatherApi(latencyMs = 0)))

    @Test
    fun selecting_a_known_city_ends_with_weather_and_no_loading() {
        val viewModel = newViewModel()
        viewModel.onIntent(WeatherIntent.SelectCity("Bukhara"))
        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals("Bukhara", state.weather?.city)
        assertNull(state.error)
    }

    @Test
    fun selecting_an_unknown_city_ends_with_error() {
        val viewModel = newViewModel()
        viewModel.onIntent(WeatherIntent.SelectCity("Atlantis"))
        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals("No forecast for Atlantis", state.error)
    }
}
