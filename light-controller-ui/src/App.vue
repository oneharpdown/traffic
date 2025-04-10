<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'
const activeLight = ref<string>('red')

const handleActiveLightChange = () => {
  axios.post('http://localhost:8080/intersections', { activeLight: activeLight.value })
    .then(console.log)
    .catch(console.error)
}
</script>

<template>
  <header>
    <div class="wrapper">
      <h1>Intersection Light Controller</h1>
    </div>
  </header>

  <main>
    <div class="light-controller">
      <div class="light">
        <label>
          <input type="radio" value="red" class="red" v-model="activeLight" name="light"
            @change="handleActiveLightChange" /> Red
        </label>
        <label>
          <input type="radio" value="yellow" class="yellow" v-model="activeLight" name="light"
            @change="handleActiveLightChange" /> Yellow
        </label>
        <label>
          <input type="radio" value="green" class="green" v-model="activeLight" name="light"
            @change="handleActiveLightChange" /> Green
        </label>
      </div>

      <p>Active light: {{ activeLight }}</p>
    </div>
  </main>
</template>

<style scoped>
header {
  line-height: 1.5;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    margin: calc(var(--section-gap) / 4);
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }
}

.light-controller {
  display: grid;
  place-items: center;
  gap: 1rem;

  .light {
    display: grid;
    gap: .5rem;
  }

}

input[type='radio'].red {
  accent-color: #cc3232;
}

input[type='radio'].yellow {
  accent-color: #e7b416;
}

input[type='radio'].green {
  accent-color: #2dc937;
}
</style>
